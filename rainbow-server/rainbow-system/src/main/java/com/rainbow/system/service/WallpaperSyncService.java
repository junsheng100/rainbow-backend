package com.rainbow.system.service;

import com.rainbow.system.entity.Wallpaper;

/**
 * 壁纸同步服务接口
 */
public interface WallpaperSyncService {

    /**
     * 同步最新壁纸数据
     */
    void syncLatestWallpapers();

    /**
     * 同步指定日期范围的壁纸数据
     */
    void syncWallpapersByDateRange(String startDate, String endDate);

    /**
     * 下载壁纸图片
     */
    boolean downloadWallpaperImage(Wallpaper wallpaper);

    /**
     * 重新下载失败的壁纸
     */
    void retryFailedDownloads();

    /**
     * 手动同步指定日期的壁纸
     */
    boolean syncWallpaperByDate(String date);


}