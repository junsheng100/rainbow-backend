package com.rainbow.system.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.system.entity.Wallpaper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface WallpaperDao extends BaseDao<Wallpaper,Long> {

  Page<Wallpaper> findAll(Specification<Wallpaper> spec, Pageable pageable);

  long count();

  Optional<Wallpaper>  findByStartDate(String today);

  Optional<Wallpaper> findTopByOrderByStartDateDesc();

  Optional<Wallpaper> findRandomWallpaper();

  void incrementViewCount(Long id);

  void incrementDownloadCount(Long id);

  Page<Wallpaper>  findByIsFeaturedTrue(Pageable pageable);

  Page<Wallpaper>  findPopularWallpapers(Pageable pageable);

  long countByDownloadStatus(int i);

  Optional<Wallpaper>findById(Long id);

  boolean existsByStartDate(String date);

  List<Wallpaper> findWallpapersToDownload();

  List<Wallpaper> findOldWallpapers(int daysToKeep);

  void deleteAll(List<Wallpaper> oldWallpapers);

  List<Wallpaper> findAll();
  
  /**
   * 保存壁纸实体
   */
  Wallpaper save(Wallpaper wallpaper);

  boolean existsByDate(String date);

  boolean existsByEndDate(String date);

  Wallpaper findByUrl(String url);

  List<Wallpaper> findLatestWallpaper(Integer size);

  List<Wallpaper> findByLogin();

  Integer findMaxOrderNum();
}
