package com.rainbow.system.controller;

import cn.hutool.core.util.IdUtil;
import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.model.base.Result;
import com.rainbow.system.entity.Wallpaper;
import com.rainbow.system.model.dto.WallpaperResponse;
import com.rainbow.system.service.SysResourceService;
import com.rainbow.system.service.WallpaperService;
import com.rainbow.system.service.WallpaperSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/wallpaper")
@Tag(name = "壁纸管理", description = "壁纸管理接口")
@Validated
public class WallpaperController extends BaseController<Wallpaper, Long, WallpaperService> {

  @Value("${wallpaper.storage.base-path:}")
  private String basePath;

  @Autowired
  private WallpaperSyncService wallpaperSyncService;
  @Autowired
  private SysResourceService resourceService;

  @OperLog
  @GetMapping("/today")
  @Operation(summary = "获取今日壁纸")
  public Result<WallpaperResponse> getTodayWallpaper() {
    try {
      WallpaperResponse wallpaper = service.getTodayWallpaper();
      if (wallpaper == null) {
        return Result.error("今日壁纸暂未更新");
      }
      return Result.success(wallpaper);
    } catch (Exception e) {
      log.error("获取今日壁纸失败", e);
      return Result.error("获取失败: " + e.getMessage());
    }
  }

  @OperLog
  @PostMapping("/sync")
  @Operation(summary = "手动同步壁纸数据")
  public Result<String> manualSync() {
    try {
      wallpaperSyncService.syncLatestWallpapers();
      return Result.success("同步任务已启动，请稍后查看结果");
    } catch (Exception e) {
      log.error("手动同步失败", e);
      return Result.error("同步失败: " + e.getMessage());
    }
  }

  @OperLog
  @Operation(summary = "文件上传")
  @PostMapping("/upload")
  @ResponseBody
  public Result<String> upload(@RequestPart("file") MultipartFile multipartFile) {
    try {

      String type = multipartFile.getContentType();
      String srcName = multipartFile.getOriginalFilename();
      String suffix = srcName.substring(srcName.lastIndexOf(ChartEnum.POINT.getCode()) + 1);
      String localPath = "/data/wallpapers/" + IdUtil.getSnowflakeNextIdStr() + "." + suffix;


      resourceService.uploadFile(multipartFile, localPath);

      return Result.success(localPath);

    } catch (Exception e) {
      log.error("文件上传失败", e);
      return Result.error("文件上传失败");
    }
  }


  @PostMapping("/download/{id}")
  @Operation(summary = "记录壁纸下载")
  public Result<String> recordDownload(@PathVariable Long id) {
    try {
      service.incrementDownloadCount(id);
      return Result.success("记录成功");
    } catch (Exception e) {
      log.error("记录下载失败, ID: {}", id, e);
      return Result.error("记录失败");
    }
  }

  @GetMapping("/public/latest")
  @Operation(summary = "取设置的壁纸")
  public Result<List<WallpaperResponse>> findLatestWallpaper(@RequestParam(name = "size", defaultValue = "8") Integer size) {
    try {
      List<WallpaperResponse> wallpaperList = service.findByLogin();
      if (CollectionUtils.isEmpty(wallpaperList)) {
        return Result.error("暂无壁纸数据");
      }
      return Result.success(wallpaperList);
    } catch (Exception e) {
      log.error("获取壁纸失败", e);
      return Result.error("获取壁纸失败: " + e.getMessage());
    }
  }


} 