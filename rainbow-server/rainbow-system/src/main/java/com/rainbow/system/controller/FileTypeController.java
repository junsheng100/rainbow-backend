package com.rainbow.system.controller;


import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.model.base.Result;
import com.rainbow.system.entity.FileType;
import com.rainbow.system.service.FileTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mime/type")
@Tag(name = "资源类型管理", description = "资源类型管理")
@Validated
public class FileTypeController extends BaseController<FileType,String, FileTypeService> {


  @OperLog
  @Operation(summary = "许可的文件类型")
  @GetMapping("/allow")
  @ResponseBody
  public Result<List<String>> findAllow() {
    List<String> list = service.findAllow();
    return  Result.success(list);
  }

  @OperLog
  @Operation(summary = "文件上传")
  @PostMapping("/upload")
  @ResponseBody
  public Result<String> upload(@RequestPart("file") MultipartFile multipartFile) {
    try {

      String srcName =multipartFile.getOriginalFilename();
      String suffix = srcName.substring(srcName.lastIndexOf(ChartEnum.POINT.getCode())+1);
//      String fileUrl = "/filetype/" + IdUtil.getSnowflakeNextIdStr() + "." + suffix;
      String fileUrl = "/filetype/" + srcName;

      service.uploadFile(multipartFile,fileUrl);

      return Result.success(fileUrl);

    } catch (Exception e) {
      log.error("文件上传失败", e);
      return Result.error("文件上传失败");
    }
  }


}
