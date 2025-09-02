package com.rainbow.system.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.system.entity.Wallpaper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface WallpaperRepository extends BaseRepository<Wallpaper, Long> {

  /**
   * 根据开始日期查找壁纸
   */
  Optional<Wallpaper> findByStartDate(String startDate);

  /**
   * 检查指定日期的壁纸是否存在
   */
  boolean existsByStartDate(String startDate);

  /**
   * 根据精选状态查找壁纸
   */
  Page<Wallpaper> findByIsFeaturedTrue(Pageable pageable);

  /**
   * 根据下载状态查找壁纸
   */
  Page<Wallpaper> findByDownloadStatus(Integer downloadStatus, Pageable pageable);

  /**
   * 查找日期范围内的壁纸
   */
  @Query("SELECT w FROM Wallpaper w " +
          " WHERE w.startDate BETWEEN :startDate " +
          " AND :endDate ORDER BY w.startDate DESC")
  List<Wallpaper> findByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

  /**
   * 获取最新的壁纸
   */
  Optional<Wallpaper> findTopByOrderByStartDateDesc();

  /**
   * 获取随机壁纸
   */
  @Query(value = "SELECT * FROM wall_paper WHERE download_status = 1 ORDER BY RAND() LIMIT 1", nativeQuery = true)
  Optional<Wallpaper> findRandomWallpaper();

  /**
   * 增加查看次数
   */
  @Modifying
  @Query("UPDATE Wallpaper w SET w.viewCount = w.viewCount + 1 WHERE w.id = :id")
  void incrementViewCount(@Param("id") Long id);

  /**
   * 增加下载次数
   */
  @Modifying
  @Query("UPDATE Wallpaper w SET w.downloadCount = w.downloadCount + 1 WHERE w.id = :id")
  void incrementDownloadCount(@Param("id") Long id);

  /**
   * 统计指定状态的壁纸数量
   */
  long countByDownloadStatus(Integer downloadStatus);

  /**
   * 查找需要下载的壁纸（未下载或下载失败的）
   */
  @Query("SELECT w FROM Wallpaper w WHERE w.downloadStatus IN (0, 2) ORDER BY w.startDate DESC")
  List<Wallpaper> findWallpapersToDownload();

  /**
   * 查找超过指定天数的旧壁纸
   */
  @Query(value = "SELECT * FROM wall_paper WHERE start_date < DATE_FORMAT(DATE_SUB(NOW(), INTERVAL :days DAY), '%Y%m%d')", nativeQuery = true)
  List<Wallpaper> findOldWallpapers(@Param("days") int days);

  /**
   * 根据哈希值查找壁纸
   */
  Optional<Wallpaper> findByHsh(String hsh);

  /**
   * 获取热门壁纸（按下载次数排序）
   */
  @Query("SELECT w FROM Wallpaper w WHERE w.downloadStatus = 1 ORDER BY w.downloadCount DESC, w.viewCount DESC")
  Page<Wallpaper> findPopularWallpapers(Pageable pageable);


  @Query("SELECT count(w.id) FROM Wallpaper w WHERE w.startDate = ?1 or w.endDate = ?1  ")
  Long existsByDate(String date);

  @Query("SELECT count(w.id) FROM Wallpaper w WHERE w.endDate = ?1  ")
  Long existsByEndDate(String date);

  @Query("SELECT w FROM Wallpaper w WHERE w.url = ?1 or w.fullUrl =?1 or w.localPath = ?1 ")
  Wallpaper findByUrl(String url);

  @Query(value = "SELECT w.* FROM wall_paper w WHERE w.status = '0' and w.download_status = 1  order by w.end_date desc limit ?1 ",nativeQuery = true)
  List<Wallpaper> findLatestWallpaper(Integer size);

  @Query(value = "SELECT w.* FROM wall_paper w WHERE w.status = '0' and w.is_login = 1  order by w.end_date desc  ",nativeQuery = true)
  List<Wallpaper> findByLogin();

  @Query(value = "SELECT max(w.orderNum) FROM Wallpaper w ")
  Integer findMaxOrderNum();


}