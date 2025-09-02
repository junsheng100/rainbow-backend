package com.rainbow.system.service.impl;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.rainbow.constant.WallpaperConstant;
import com.rainbow.system.entity.Wallpaper;
import com.rainbow.system.model.dto.BingApiResponse;
import com.rainbow.system.model.dto.BingImageDto;
import com.rainbow.system.resource.SysConfigDao;
import com.rainbow.system.resource.WallpaperDao;
import com.rainbow.system.service.WallpaperSyncService;
import com.rainbow.system.utils.WallPaperFileStorageUtil;
import com.rainbow.system.utils.WallpaperUrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class WallpaperSyncServiceImpl implements WallpaperSyncService {

  private static final Logger log = LoggerFactory.getLogger(WallpaperSyncServiceImpl.class);

//  @Value("${wallpaper.storage.base-path:}")
//  private String basePath;

  @Autowired
  private WallpaperDao baseDao;

  @Autowired
  private SysConfigDao configDao;


  @Autowired
  private WallPaperFileStorageUtil wallPaperFileStorageUtil;

  @Autowired
  private WallpaperUrlUtil wallpaperUrlUtil;

  @Autowired
  private Executor wallpaperDownloadExecutor;

  private final static String githubApiUrl = "https://raw.onmicrosoft.cn/Bing-Wallpaper-Action/main/data/zh-CN_update.json";

  private final static int maxRetryTimes = 3;

  private final static long retryDelay = 5000L;




  @Override
  public void syncLatestWallpapers() {
    log.info("开始同步最新壁纸数据");

    try {
      // 使用 HuTool 直接调用 API 避免复杂的HTTP处理
      String apiUrl = githubApiUrl + "?format=js&idx=0&n=8&mkt=zh-CN";
      log.info("尝试从 Bing API 获取数据: {}", apiUrl);

      String result = HttpUtil.get(apiUrl, CharsetUtil.CHARSET_UTF_8);

      if (StringUtils.hasText(result)) {
        log.debug("获取到响应数据，长度: {} 字符", result.length());

        BingApiResponse response = JSON.parseObject(result, BingApiResponse.class);

        if (response != null && response.getImages() != null && !response.getImages().isEmpty()) {
          processBingImages(response.getImages());
          log.info("从 Bing API 成功同步了 {} 张壁纸", response.getImages().size());
        } else {
          log.warn("Bing API 返回数据为空或解析失败");
        }
      } else {
        log.error("无法获取 Bing API 数据");
      }

    } catch (Exception e) {
      log.error("同步最新壁纸数据失败", e);
    }
  }

  @Override
  public void syncWallpapersByDateRange(String startDate, String endDate) {
    log.info("开始同步日期范围壁纸: {} 到 {}", startDate, endDate);

    try {
      LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.BASIC_ISO_DATE);
      LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.BASIC_ISO_DATE);

      while (!start.isAfter(end)) {
        String dateStr = start.format(DateTimeFormatter.BASIC_ISO_DATE);
        syncWallpaperByDate(dateStr);
        start = start.plusDays(1);

        // 避免请求过于频繁
        Thread.sleep(1000);
      }

      log.info("日期范围同步完成");
    } catch (Exception e) {
      log.error("同步日期范围壁纸失败", e);
    }
  }

  @Override
  public boolean downloadWallpaperImage(Wallpaper wallpaper) {
    if (wallpaper == null) {
      return false;
    }

    // 直接使用反射方式获取URL，避免getter方法问题
    String url = getWallpaperUrl(wallpaper);
    if (!StringUtils.hasText(url)) {
      return false;
    }

    String imageUrl = wallpaperUrlUtil.buildFullImageUrl(url);
    log.info("开始下载壁纸图片: {}", imageUrl);

    int retryCount = 0;
    while (retryCount < maxRetryTimes) {
      try {
        String startDate = getWallpaperStartDate(wallpaper);

        String fileName = wallPaperFileStorageUtil.generateFileName(startDate, "jpg");

        String localPath = WallpaperConstant.STORE_PATH +fileName;
        String fileBasePath = wallPaperFileStorageUtil.getBasePath();
        String filePath = fileBasePath +File.separator+ localPath;
        File file = new File(filePath);
        File fdir = file.getParentFile();
        if (!fdir.exists())
          fdir.mkdirs();

        long imageData = HttpUtil.downloadFile(imageUrl, filePath);

        if (imageData > 0) {

          try {
            wallpaper.setLocalPath(localPath);
            wallpaper.setFileSize(imageData);
            wallpaper.setDownloadStatus(1);
            baseDao.save(wallpaper);


            log.info("壁纸下载成功: {}, 文件大小: {} bytes", localPath, imageData);
            return true;
          } catch (Exception e) {
            log.error("保存壁纸记录失败", e);
            return false;
          }
        }

      } catch (Exception e) {
        retryCount++;
        log.warn("下载壁纸失败，第 {} 次重试: {}", retryCount, e.getMessage());

        if (retryCount < maxRetryTimes) {
          try {
            Thread.sleep(retryDelay);
          } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            break;
          }
        }
      }
    }

    // 标记为下载失败
    try {
      updateWallpaperDownloadStatus(wallpaper, 2);
      baseDao.save(wallpaper);
    } catch (Exception e) {
      log.error("更新下载失败状态时出错", e);
    }

    log.error("壁纸下载失败，已达到最大重试次数: {}", imageUrl);
    return false;
  }

  @Override
  public void retryFailedDownloads() {
    log.info("开始重试失败的下载");

    try {
      List<Wallpaper> failedWallpapers = baseDao.findWallpapersToDownload();

      if (failedWallpapers.isEmpty()) {
        log.info("没有需要重试下载的壁纸");
        return;
      }

      log.info("找到 {} 个需要重试下载的壁纸", failedWallpapers.size());

      // 异步下载，避免阻塞
      failedWallpapers.forEach(wallpaper -> {
        CompletableFuture.runAsync(() -> {
          downloadWallpaperImage(wallpaper);
        }, wallpaperDownloadExecutor);
      });
    } catch (Exception e) {
      log.error("重试失败的下载时出错", e);
    }
  }

  @Override
  public boolean syncWallpaperByDate(String date) {
    if (!StringUtils.hasText(date) || date.length() != 8) {
      log.error("日期格式错误: {}", date);
      return false;
    }

    // 检查是否已存在
    try {
      if (baseDao.existsByEndDate(date)) {
        log.info("日期 {} 的壁纸已存在，跳过同步", date);
        return true;
      }
    } catch (Exception e) {
      log.error("检查壁纸是否存在时出错", e);
      return false;
    }

    try {
      // 构建特定日期的API URL
      String apiUrl = buildDateSpecificApiUrl(date);
      String result = HttpUtil.get(apiUrl, CharsetUtil.CHARSET_UTF_8);

      BingApiResponse response = JSON.parseObject(result, BingApiResponse.class);

      if (response != null && response.getImages() != null && !response.getImages().isEmpty()) {
        BingImageDto imageDto = response.getImages().get(0);
        Wallpaper wallpaper = convertToEntity(imageDto);

        // 设置创建和更新时间
        setWallpaperTimestamps(wallpaper);

        try {
          baseDao.save(wallpaper);

          // 异步下载图片
          CompletableFuture.runAsync(() -> {
            downloadWallpaperImage(wallpaper);
          }, wallpaperDownloadExecutor);

          log.info("成功同步日期 {} 的壁纸", date);
          return true;
        } catch (Exception e) {
          log.error("保存壁纸时出错", e);
          return false;
        }
      }

    } catch (Exception e) {
      log.error("同步日期 {} 的壁纸失败", date, e);
    }

    return false;
  }




  /**
   * 处理 图片数据
   */
  private void processBingImages(List<BingImageDto> images) {
    Integer maxOrderNum = baseDao.findMaxOrderNum();
    maxOrderNum = maxOrderNum == null ? 1 : maxOrderNum+1;
    for (BingImageDto imageDto : images) {
      try {
        if (baseDao.existsByStartDate(imageDto.getStartdate())) {
          log.debug("壁纸已存在，跳过: {}", imageDto.getStartdate());
          continue;
        }

        Wallpaper wallpaper = convertToEntity(imageDto);
        wallpaper.setOrderNum(maxOrderNum);
        maxOrderNum++;
        try {
          baseDao.save(wallpaper);
          // 异步下载图片
          CompletableFuture.runAsync(() -> {
            downloadWallpaperImage(wallpaper);
          }, wallpaperDownloadExecutor);
        } catch (Exception e) {
          e.printStackTrace();
          log.error("保存壁纸失败: {}", imageDto.getStartdate(), e);
        }

      } catch (Exception e) {
        log.error("处理壁纸数据失败: {}", imageDto.getStartdate(), e);
      }
    }
  }

  /**
   * 转换 DTO 为实体
   */
  private Wallpaper convertToEntity(BingImageDto dto) {
    Wallpaper entity = new Wallpaper();

    // 使用反射或直接字段访问来设置值
    setWallpaperFields(entity, dto);

    return entity;
  }

  /**
   * 构建特定日期的API URL
   */
  private String buildDateSpecificApiUrl(String date) {
    LocalDate targetDate = LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE);
    LocalDate today = LocalDate.now();
    long daysDiff = java.time.temporal.ChronoUnit.DAYS.between(targetDate, today);

    return githubApiUrl + "?format=js&idx=" + daysDiff + "&n=1&mkt=zh-CN";
  }

  /**
   * 验证单个壁纸数据
   */
  private boolean validateSingleWallpaper(Wallpaper wallpaper) {
    try {
      String startDate = getWallpaperStartDate(wallpaper);
      String url = getWallpaperUrl(wallpaper);

      if (!StringUtils.hasText(startDate) || !StringUtils.hasText(url)) {
        log.warn("壁纸数据缺少必要字段");
        return false;
      }

      String localPath = getWallpaperLocalPath(wallpaper);
      if (StringUtils.hasText(localPath)) {
        if (!wallPaperFileStorageUtil.exists(localPath)) {
          log.warn("壁纸本地文件不存在: {}", localPath);
          updateWallpaperDownloadStatus(wallpaper, 0);
          baseDao.save(wallpaper);
          return false;
        }
      }

      return true;

    } catch (Exception e) {
      log.error("验证壁纸数据失败", e);
      return false;
    }
  }

  // 使用反射来获取和设置字段值，避免 Lombok 问题
  private String getWallpaperUrl(Wallpaper wallpaper) {
    try {
      java.lang.reflect.Field field = Wallpaper.class.getDeclaredField("url");
      field.setAccessible(true);
      return (String) field.get(wallpaper);
    } catch (Exception e) {
      log.error("获取wallpaper url失败", e);
      return null;
    }
  }

  private String getWallpaperStartDate(Wallpaper wallpaper) {
    try {
      java.lang.reflect.Field field = Wallpaper.class.getDeclaredField("startDate");
      field.setAccessible(true);
      return (String) field.get(wallpaper);
    } catch (Exception e) {
      log.error("获取wallpaper startDate失败", e);
      return null;
    }
  }

  private String getWallpaperLocalPath(Wallpaper wallpaper) {
    try {
      java.lang.reflect.Field field = Wallpaper.class.getDeclaredField("localPath");
      field.setAccessible(true);
      return (String) field.get(wallpaper);
    } catch (Exception e) {
      log.error("获取wallpaper localPath失败", e);
      return null;
    }
  }

//  private void updateWallpaperInfo(BingWallpaper wallpaper, String localPath, Long fileSize, Integer downloadStatus) {
//    try {
//      java.lang.reflect.Field localPathField = BingWallpaper.class.getDeclaredField("localPath");
//      localPathField.setAccessible(true);
//      localPathField.set(wallpaper, localPath);
//
//      java.lang.reflect.Field fileSizeField = BingWallpaper.class.getDeclaredField("fileSize");
//      fileSizeField.setAccessible(true);
//      fileSizeField.set(wallpaper, fileSize);
//
//      java.lang.reflect.Field downloadStatusField = BingWallpaper.class.getDeclaredField("downloadStatus");
//      downloadStatusField.setAccessible(true);
//      downloadStatusField.set(wallpaper, downloadStatus);
//
//      java.lang.reflect.Field downloadTimeField = BingWallpaper.class.getDeclaredField("downloadTime");
//      downloadTimeField.setAccessible(true);
//      downloadTimeField.set(wallpaper, LocalDateTime.now());
//    } catch (Exception e) {
//      log.error("更新wallpaper信息失败", e);
//    }
//  }

  private void updateWallpaperDownloadStatus(Wallpaper wallpaper, Integer status) {
    try {
      java.lang.reflect.Field field = Wallpaper.class.getDeclaredField("downloadStatus");
      field.setAccessible(true);
      field.set(wallpaper, status);
    } catch (Exception e) {
      log.error("更新wallpaper下载状态失败", e);
    }
  }

  private void setWallpaperTimestamps(Wallpaper wallpaper) {
    try {
      LocalDateTime now = LocalDateTime.now();
      java.lang.reflect.Field fcdField = Wallpaper.class.getSuperclass().getDeclaredField("fcd");
      fcdField.setAccessible(true);
      fcdField.set(wallpaper, now);

      java.lang.reflect.Field lcdField = Wallpaper.class.getSuperclass().getDeclaredField("lcd");
      lcdField.setAccessible(true);
      lcdField.set(wallpaper, now);
    } catch (Exception e) {
      log.error("设置wallpaper时间戳失败", e);
    }
  }

  private void setWallpaperFields(Wallpaper entity, BingImageDto dto) {
    try {
      // 设置各个字段
      setField(entity, "startDate", dto.getStartdate());
      setField(entity, "fullStartDate", dto.getFullstartdate());
      setField(entity, "endDate", dto.getEnddate());
      setField(entity, "url", dto.getUrl());
      setField(entity, "urlBase", dto.getUrlbase());
      setField(entity, "copyright", dto.getCopyright());
      setField(entity, "copyrightLink", dto.getCopyrightlink());
      setField(entity, "title", dto.getTitle());
      setField(entity, "quiz", dto.getQuiz());
      setField(entity, "wp", dto.getWp());
      setField(entity, "hsh", dto.getHsh());
      setField(entity, "drk", dto.getDrk());
      setField(entity, "top", dto.getTop());
      setField(entity, "bot", dto.getBot());

      // 构建完整URL
      setField(entity, "fullUrl", wallpaperUrlUtil.buildFullImageUrl(dto.getUrl()));

      // 设置默认值
      setField(entity, "downloadStatus", 0);
      setField(entity, "viewCount", 0);
      setField(entity, "downloadCount", 0);
      setField(entity, "isFeatured", false);

    } catch (Exception e) {
      log.error("设置wallpaper字段失败", e);
    }
  }

  private void setField(Object obj, String fieldName, Object value) {
    try {
      java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(obj, value);
    } catch (Exception e) {
      log.debug("设置字段 {} 失败: {}", fieldName, e.getMessage());
    }
  }
}