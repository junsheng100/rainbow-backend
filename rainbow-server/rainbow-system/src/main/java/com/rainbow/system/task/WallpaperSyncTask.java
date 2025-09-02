package com.rainbow.system.task;

import com.rainbow.base.annotation.JobTask;
import com.rainbow.system.service.WallpaperSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "wallpaper.sync.enabled", havingValue = "true", matchIfMissing = true)
public class WallpaperSyncTask {

    @Autowired
    private WallpaperSyncService wallpaperSyncService;

    /**
     * 每日11:30执行壁纸同步任务
     */

    @JobTask("syncDailyWallpapers")
//    @Scheduled(cron = "0 30 11 * * ?")
//    @Async("wallpaperTaskExecutor")
    public void syncDailyWallpapers() {
        log.info("开始执行每日壁纸同步任务");
        long startTime = System.currentTimeMillis();
        
        try {
            wallpaperSyncService.syncLatestWallpapers();
            long duration = System.currentTimeMillis() - startTime;
            log.info("每日壁纸同步任务执行成功，耗时: {}ms", duration);
        } catch (Exception e) {
            log.error("每日壁纸同步任务执行失败", e);
            // 这里可以添加告警通知，比如发送邮件、企业微信等
        }
    }


    /**
     * 每小时检查失败的下载并重试
     */
    @Scheduled(cron = "0 0 * * * ?")
    @Async("wallpaperTaskExecutor")
    public void retryFailedDownloads() {
        log.info("开始执行失败下载重试任务");
        
        try {
            wallpaperSyncService.retryFailedDownloads();
            log.info("失败下载重试任务执行完成");
        } catch (Exception e) {
            log.error("失败下载重试任务执行失败", e);
        }
    }


} 