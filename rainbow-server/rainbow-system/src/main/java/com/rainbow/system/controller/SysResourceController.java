package com.rainbow.system.controller;

import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.Result;
import com.rainbow.system.entity.SysConfig;
import com.rainbow.system.entity.SysResource;
import com.rainbow.system.service.SysConfigService;
import com.rainbow.system.service.SysResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/files")
@Tag(name = "系统资源管理", description = "系统资源管理")
@Validated
public class SysResourceController extends BaseController<SysResource,Long,SysResourceService> {

  @Autowired
  private SysConfigService configService;

  @OperLog
  @Operation(summary = "文件上传")
  @PostMapping("/upload")
  @ResponseBody
  public Result<String> upload(@RequestPart("file") MultipartFile multipartFile) {
    try {

      String data = service.uploadFile(multipartFile);
      return Result.success(data);
    } catch (Exception e) {
      log.error("文件上传失败", e);
      return Result.error("文件上传失败:" + e.getMessage());
    }
  }


  @Operation(summary = "获取预览的 URL")
  @GetMapping("/preview/url")
  @ResponseBody
  public Result<String> getPreviewUrl() {
    try {
      SysConfig data = configService.getConfigValue("file.preview.url");
      if(null == data)
        return Result.error("Data is null");
      return Result.success(data.getConfigValue());
    } catch (Exception e) {
      log.error("获取预览的URL ", e);
      return Result.error("获取预览的:" + e.getMessage());
    }
  }




}
