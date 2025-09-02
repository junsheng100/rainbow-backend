package com.rainbow.system.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.system.entity.Wallpaper;
import com.rainbow.system.model.dto.WallpaperQueryRequest;
import com.rainbow.system.model.dto.WallpaperResponse;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 壁纸服务接口
 */
public interface WallpaperService extends BaseService<Wallpaper,Long> {

  /**
   * 分页查询壁纸列表
   */
  Page<WallpaperResponse> queryWallpapers(WallpaperQueryRequest request);

  /**
   * 根据ID获取壁纸详情
   */
  WallpaperResponse getWallpaperById(Long id);

  /**
   * 获取今日壁纸
   */
  WallpaperResponse getTodayWallpaper();

  /**
   * 获取最新壁纸
   */
  WallpaperResponse getLatestWallpaper();

  /**
   * 获取随机壁纸
   */
  WallpaperResponse getRandomWallpaper();

  /**
   * 增加查看次数
   */
  void incrementViewCount(Long id);

  /**
   * 增加下载次数
   */
  void incrementDownloadCount(Long id);

  /**
   * 获取精选壁纸列表
   */
  List<WallpaperResponse> getFeaturedWallpapers(Integer limit);

  /**
   * 获取热门壁纸列表
   */
  List<WallpaperResponse> getPopularWallpapers(Integer limit);

  /**
   * 搜索壁纸
   */
  Page<WallpaperResponse> searchWallpapers(String keyword, int page, int size);

  /**
   * 获取统计信息
   */
  WallpaperStatistics getStatistics();

  List<WallpaperResponse> findLatestWallpaper(Integer size);

  List<WallpaperResponse>  findByLogin();



  /**
   * 内部类：壁纸统计信息
   */
  class WallpaperStatistics {
    private long totalCount;
    private long downloadedCount;
    private long failedCount;
    private long pendingCount;
    private long totalViewCount;
    private long totalDownloadCount;

    // 构造函数
    public WallpaperStatistics(long totalCount, long downloadedCount, long failedCount,
                               long pendingCount, long totalViewCount, long totalDownloadCount) {
      this.totalCount = totalCount;
      this.downloadedCount = downloadedCount;
      this.failedCount = failedCount;
      this.pendingCount = pendingCount;
      this.totalViewCount = totalViewCount;
      this.totalDownloadCount = totalDownloadCount;
    }

    // Getters and Setters
    public long getTotalCount() {
      return totalCount;
    }

    public void setTotalCount(long totalCount) {
      this.totalCount = totalCount;
    }

    public long getDownloadedCount() {
      return downloadedCount;
    }

    public void setDownloadedCount(long downloadedCount) {
      this.downloadedCount = downloadedCount;
    }

    public long getFailedCount() {
      return failedCount;
    }

    public void setFailedCount(long failedCount) {
      this.failedCount = failedCount;
    }

    public long getPendingCount() {
      return pendingCount;
    }

    public void setPendingCount(long pendingCount) {
      this.pendingCount = pendingCount;
    }

    public long getTotalViewCount() {
      return totalViewCount;
    }

    public void setTotalViewCount(long totalViewCount) {
      this.totalViewCount = totalViewCount;
    }

    public long getTotalDownloadCount() {
      return totalDownloadCount;
    }

    public void setTotalDownloadCount(long totalDownloadCount) {
      this.totalDownloadCount = totalDownloadCount;
    }
  }
} 