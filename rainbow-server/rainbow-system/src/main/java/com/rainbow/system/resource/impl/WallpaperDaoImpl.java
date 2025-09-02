package com.rainbow.system.resource.impl;

import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.system.entity.Wallpaper;
import com.rainbow.system.repository.WallpaperRepository;
import com.rainbow.system.resource.WallpaperDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class WallpaperDaoImpl extends BaseDaoImpl<Wallpaper,Long, WallpaperRepository> implements WallpaperDao {

  private static final Logger log = LoggerFactory.getLogger(WallpaperDaoImpl.class);

  @Override
  public Page<Wallpaper> findAll(Specification<Wallpaper> spec, Pageable pageable) {
    return super.jpaRepository.findAll(spec,pageable);
  }

  @Override
  public long count() {
    return jpaRepository.count();
  }

  @Override
  public  Optional<Wallpaper> findByStartDate(String today) {
    return  jpaRepository.findByStartDate(today);
  }

  @Override
  public Optional<Wallpaper> findTopByOrderByStartDateDesc() {
    return jpaRepository.findTopByOrderByStartDateDesc();
  }

  @Override
  public Optional<Wallpaper> findRandomWallpaper() {
    return jpaRepository.findRandomWallpaper();
  }

  @Override
  public void incrementViewCount(Long id) {
    jpaRepository.incrementViewCount(id);
  }

  @Override
  public void incrementDownloadCount(Long id) {
    jpaRepository.incrementDownloadCount(id);
  }

  @Override
  public Page<Wallpaper> findByIsFeaturedTrue(Pageable pageable) {
    return jpaRepository.findByIsFeaturedTrue(pageable);
  }

  @Override
  public Page<Wallpaper> findPopularWallpapers(Pageable pageable) {
    return jpaRepository.findPopularWallpapers(pageable);
  }

  @Override
  public long countByDownloadStatus(int i) {
    return jpaRepository.countByDownloadStatus(i);
  }

  @Override
  public Optional<Wallpaper> findById(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public boolean existsByStartDate(String date) {
    return jpaRepository.existsByStartDate(date);
  }

  @Override
  public List<Wallpaper> findWallpapersToDownload() {
    return jpaRepository.findWallpapersToDownload();
  }

  @Override
  public List<Wallpaper> findOldWallpapers(int daysToKeep) {
    return jpaRepository.findOldWallpapers(daysToKeep);
  }

  @Override
  public void deleteAll(List<Wallpaper> oldWallpapers) {
    jpaRepository.deleteAll(oldWallpapers);
  }

  @Override
  public List<Wallpaper> findAll() {
    return jpaRepository.findAll();
  }
  
  /**
   * 保存壁纸实体
   */
  public Wallpaper save(Wallpaper wallpaper) {
    return super.save(wallpaper);
  }

  @Override
  public boolean existsByDate(String date) {
    Long count = jpaRepository.existsByDate(date);
    return count >0;
  }

  @Override
  public boolean existsByEndDate(String date) {
    Long count = jpaRepository.existsByEndDate(date);
    return count >0;
  }

  @Override
  public Wallpaper findByUrl(String url) {
      return StringUtils.isBlank(url)?null:jpaRepository.findByUrl(url);
  }

  @Override
  public List<Wallpaper> findLatestWallpaper(Integer size) {
    size = null == size?10:size;
    return jpaRepository.findLatestWallpaper(size);
  }

  @Override
  public List<Wallpaper> findByLogin() {
    return jpaRepository.findByLogin();
  }

  @Override
  public Integer findMaxOrderNum() {
    return jpaRepository.findMaxOrderNum();
  }
}
