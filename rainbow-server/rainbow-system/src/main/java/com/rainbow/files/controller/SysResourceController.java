package com.rainbow.files.controller;

import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.files.entity.SysResource;
import com.rainbow.files.service.SysResourceService;
import com.rainbow.system.entity.SysDictData;
import com.rainbow.system.model.dto.ResourceConfig;
import com.rainbow.system.service.SysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/files")
@Tag(name = "系统资源管理", description = "系统资源管理")
@Validated
public class SysResourceController extends BaseController<SysResource, String , SysResourceService> {

  @Autowired
  private SysDictDataService dictDataService;



  @Operation(summary = "获取文件管理的参数")
  @GetMapping("/config")
  @ResponseBody
  public Result<ResourceConfig> getUploadConfig() {
    try {
      ResourceConfig config = new ResourceConfig();
      List<SysDictData> dataList = dictDataService.findByType("file_config");
      if (CollectionUtils.isEmpty(dataList))
        return Result.error("Data is null");

      for (SysDictData data : dataList) {
        if ("file.upload.url".equals(data.getDictLabel())) {
          config.setUploadUrl(data.getDictValue());
        } else if ("file.preview.url".equals(data.getDictLabel())) {
          config.setPreviewUrl(data.getDictValue());
        } else if ("file.max.size".equals(data.getDictLabel())) {
          config.setMaxSize(Long.parseLong(data.getDictValue()));
        } else if ("file.preview.type".equals(data.getDictLabel())) {
          String value = data.getDictValue();
          if (StringUtils.isNotBlank(value)) {
            config.setAllowExtensions(value.split(ChartEnum.COMMA.getCode()));
          }
        }
      }
      return Result.success(config);
    } catch (Exception e) {
      log.error("获取预览的URL ", e);
      return Result.error("获取预览的:" + e.getMessage());
    }
  }


  @OperLog
  @Operation(summary = "Minio 文件上传")
  @PostMapping("/upload")
  @ResponseBody
  public Result<String> upload(@RequestPart("file") MultipartFile multipartFile) {
    try {
      String data = service.uploadFile(multipartFile);
      return Result.success(data);
    } catch (Exception e) {
      e.printStackTrace();
      log.error("文件上传失败", e);
      return Result.error("文件上传失败:" + e.getMessage());
    }
  }




}
