package com.rainbow.files.controller;

import com.alibaba.fastjson2.JSON;
import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.files.entity.SysResource;
import com.rainbow.files.service.SysResourceService;
import com.rainbow.system.entity.SysConfig;
import com.rainbow.system.entity.SysDictData;
import com.rainbow.files.config.ResourceConfig;
import com.rainbow.system.service.SysConfigService;
import com.rainbow.system.service.SysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/files")
@Tag(name = "系统资源管理", description = "系统资源管理")
@Validated
public class SysResourceController extends BaseController<SysResource, String, SysResourceService> {


  @Operation(summary = "获取文件管理的参数")
  @GetMapping("/config")
  @ResponseBody
  public Result<ResourceConfig> getUploadConfig() {
    try {
      ResourceConfig config = service.getResourceConfig();
      return Result.success(config);
    } catch (Exception e) {
      log.error("获取预览的URL ", e);
      return Result.error("获取预览的:" + e.getMessage());
    }
  }


  @Operation(summary = "设置文件管理的参数")
  @PostMapping("/config")
  @ResponseBody
  public Result<ResourceConfig> setUploadConfig(@RequestBody ResourceConfig config) {
    try {
      service.setUploadConfig(config);
      return Result.success(config);
    } catch (Exception e) {
      e.printStackTrace();
      log.error("获取预览的URL ", e);
      return Result.error("获取预览的:" + e.getMessage());
    }
  }



  @Operation(summary = " 文件上传")
  @PostMapping("/upload")
  @ResponseBody
  public Result<String> upload(@RequestPart("file") MultipartFile multipartFile) {
    try {
      if (service.validate(multipartFile)) {
        String data = service.uploadFile(multipartFile);
        return Result.success(data);
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.error("文件上传失败", e);
      return Result.error( e.getMessage());
    }
    return Result.error("文件上传失败");
  }


}
