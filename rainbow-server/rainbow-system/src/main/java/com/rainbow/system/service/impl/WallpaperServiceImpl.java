package com.rainbow.system.service.impl;

import com.rainbow.base.exception.BizException;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.base.utils.CommonUtils;
import com.rainbow.system.entity.Wallpaper;
import com.rainbow.system.model.dto.WallpaperQueryRequest;
import com.rainbow.system.model.dto.WallpaperResponse;
import com.rainbow.system.resource.WallpaperDao;
import com.rainbow.system.service.WallpaperService;
import com.rainbow.system.utils.WallPaperFileStorageUtil;
import com.rainbow.system.utils.WallpaperUrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class WallpaperServiceImpl extends BaseServiceImpl<Wallpaper, Long, WallpaperDao> implements WallpaperService {


  @Autowired
  private WallPaperFileStorageUtil wallPaperFileStorageUtil;

  @Autowired
  private WallpaperUrlUtil wallpaperUrlUtil;

  @Autowired
  private Executor wallpaperDownloadExecutor;


  @Override
  public Page<WallpaperResponse> queryWallpapers(WallpaperQueryRequest request) {
    // 构建分页参数
    Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

    // 构建查询条件
    Specification<Wallpaper> spec = createSpecification(request);

    // 执行查询
    Page<Wallpaper> wallpaperPage = baseDao.findAll(spec, pageable);

    // 转换为响应对象
    List<WallpaperResponse> responseList = wallpaperPage.getContent().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());

    return new PageImpl<>(responseList, pageable, wallpaperPage.getTotalElements());
  }

  @Override
  public WallpaperResponse getWallpaperById(Long id) {
    Wallpaper wallpaper = baseDao.findById(id)
            .orElseThrow(() -> new BizException("壁纸不存在，ID: " + id));
    return convertToResponse(wallpaper);
  }

  @Override
  @Cacheable(value = "wallpaper:today", unless = "#result == null")
  public WallpaperResponse getTodayWallpaper() {
    String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    return baseDao.findByStartDate(today)
            .map(this::convertToResponse)
            .orElse(null);
  }

  @Override
//  @Cacheable(value = "wallpaper:latest", unless = "#result == null")
  public WallpaperResponse getLatestWallpaper() {
    return baseDao.findTopByOrderByStartDateDesc()
            .map(this::convertToResponse)
            .orElse(null);
  }

  @Override
  public WallpaperResponse getRandomWallpaper() {
    return baseDao.findRandomWallpaper()
            .map(this::convertToResponse)
            .orElse(null);
  }

  @Override
  @Transactional
  public void incrementViewCount(Long id) {
    baseDao.incrementViewCount(id);
  }

  @Override
  @Transactional
  public void incrementDownloadCount(Long id) {
    baseDao.incrementDownloadCount(id);
  }

  @Override
  @Cacheable(value = "wallpaper:featured", unless = "#result.isEmpty()")
  public List<WallpaperResponse> getFeaturedWallpapers(Integer limit) {
    Pageable pageable = PageRequest.of(0, limit, Sort.by("startDate").descending());
    return baseDao.findByIsFeaturedTrue(pageable)
            .map(this::convertToResponse)
            .getContent();
  }

  @Override
  @Cacheable(value = "wallpaper:popular", unless = "#result.isEmpty()")
  public List<WallpaperResponse> getPopularWallpapers(Integer limit) {
    Pageable pageable = PageRequest.of(0, limit);
    return baseDao.findPopularWallpapers(pageable)
            .map(this::convertToResponse)
            .getContent();
  }

  @Override
  public Page<WallpaperResponse> searchWallpapers(String keyword, int page, int size) {
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("startDate").descending());

    Specification<Wallpaper> spec = (root, query, criteriaBuilder) -> {
      if (!StringUtils.hasText(keyword)) {
        return criteriaBuilder.conjunction();
      }

      String likePattern = "%" + keyword.trim() + "%";
      return criteriaBuilder.or(
              criteriaBuilder.like(root.get("title"), likePattern),
              criteriaBuilder.like(root.get("copyright"), likePattern)
      );
    };

    Page<Wallpaper> wallpaperPage = baseDao.findAll(spec, pageable);

    List<WallpaperResponse> responseList = wallpaperPage.getContent().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());

    return new PageImpl<>(responseList, pageable, wallpaperPage.getTotalElements());
  }

  @Override
  public WallpaperStatistics getStatistics() {
    long totalCount = baseDao.count();
    long downloadedCount = baseDao.countByDownloadStatus(1);
    long failedCount = baseDao.countByDownloadStatus(2);
    long pendingCount = baseDao.countByDownloadStatus(0);

    // 这里可以优化为单次查询获取总的查看次数和下载次数
    long totalViewCount = 0; // TODO: 实现统计查询
    long totalDownloadCount = 0; // TODO: 实现统计查询

    return new WallpaperStatistics(totalCount, downloadedCount, failedCount,
            pendingCount, totalViewCount, totalDownloadCount);
  }

  @Override
  public List<WallpaperResponse> findLatestWallpaper(Integer size) {
    List<WallpaperResponse> responseList = new ArrayList<>();
    List<Wallpaper> list = baseDao.findLatestWallpaper(size);

    if (CollectionUtils.isNotEmpty(list)) {
      responseList = list.stream().map(t -> {
        WallpaperResponse data = new WallpaperResponse();
        BeanUtils.copyProperties(t, data, CommonUtils.getNullPropertyNames(t));
        return data;
      }).collect(Collectors.toList());
    }
    return responseList;
  }

  @Override
  public List<WallpaperResponse> findByLogin() {
    List<WallpaperResponse> responseList = new ArrayList<>();
    List<Wallpaper> list = baseDao.findByLogin();
    if (CollectionUtils.isNotEmpty(list)) {
      responseList = list.stream().map(t -> {
        WallpaperResponse data = new WallpaperResponse();
        BeanUtils.copyProperties(t, data, CommonUtils.getNullPropertyNames(t));
        return data;
      }).collect(Collectors.toList());
    }
    return responseList;
  }



  @Override
  public Boolean delete(Long id) {
    return baseDao.remove(id);
  }

  /**
   * 创建查询条件
   */
  private Specification<Wallpaper> createSpecification(WallpaperQueryRequest request) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      // 开始日期条件
      if (StringUtils.hasText(request.getStartDate())) {
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                root.get("startDate"), request.getStartDate()));
      }

      // 结束日期条件
      if (StringUtils.hasText(request.getEndDate())) {
        predicates.add(criteriaBuilder.lessThanOrEqualTo(
                root.get("startDate"), request.getEndDate()));
      }

      // 精选状态条件
      if (request.getIsFeatured() != null) {
        predicates.add(criteriaBuilder.equal(
                root.get("isFeatured"), request.getIsFeatured()));
      }

      // 下载状态条件
      if (request.getDownloadStatus() != null) {
        predicates.add(criteriaBuilder.equal(
                root.get("downloadStatus"), request.getDownloadStatus()));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  /**
   * 转换为响应对象
   */
  private WallpaperResponse convertToResponse(Wallpaper wallpaper) {
    WallpaperResponse response = new WallpaperResponse();
    BeanUtils.copyProperties(wallpaper, response);

    // 构建图片URL
    if (StringUtils.hasText(wallpaper.getLocalPath())) {
      response.setPreviewUrl(wallPaperFileStorageUtil.getImageUrl(wallpaper.getLocalPath()));
      response.setDownloadUrl(wallPaperFileStorageUtil.getImageUrl(wallpaper.getLocalPath()));
    } else if (StringUtils.hasText(wallpaper.getUrl())) {
      response.setFullUrl(wallpaperUrlUtil.buildFullImageUrl(wallpaper.getUrl()));
      response.setPreviewUrl(wallpaperUrlUtil.buildThumbnailUrl(wallpaper.getUrl()));
    }

    return response;
  }



} 