package com.rainbow.files.controller;


import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.model.base.Result;
import com.rainbow.files.entity.FileType;
import com.rainbow.files.model.FileTypeModel;
import com.rainbow.files.service.FileTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/mime/type")
@Tag(name = "资源类型管理", description = "资源类型管理")
@Validated
public class FileTypeController extends BaseController<FileType, String, FileTypeService> {


  @Operation(summary = "许可的文件类型")
  @GetMapping("/allow")
  @ResponseBody
  public Result<List<FileTypeModel>> findAllow() {
    List<FileType> typeList = service.findAllowType();
    List<FileTypeModel> list = new ArrayList<>();

    if (CollectionUtils.isNotEmpty(typeList)) {

      for (FileType type : typeList) {
        FileTypeModel model = new FileTypeModel();
        BeanUtils.copyProperties(type, model);
        list.add(model);
      }
    }

    return Result.success(list);
  }

  @Operation(summary = "许可的文件类型分组")
  @GetMapping("/allow/group")
  @ResponseBody
  public Result<Map<String, List<String>>> findAllowGroup() {
    List<FileType> typeList = service.findAllowType();
    Map<String, List<String>> map = new LinkedHashMap<>();

    if (CollectionUtils.isNotEmpty(typeList)) {
     List<String> groupList = typeList.stream().map(FileType::getTypeGroup).distinct().collect(Collectors.toList());

     for (String group : groupList){
       List<String> list = typeList.stream().filter(item -> item.getTypeGroup().equals(group)).map(FileType::getExtension).collect(Collectors.toList());
       map.put(group, list);
     }
    }

    return Result.success(map);
  }

  @Operation(summary = "许可的文件类型数据")
  @GetMapping("/allow/data")
  @ResponseBody
  public Result<List<FileType>> findAllowTypeData() {
    List<FileType> typeList = service.findAllowType();
    typeList = CollectionUtils.isEmpty(typeList) ? new ArrayList<>() : typeList;

    return Result.success(typeList);
  }

  @OperLog
  @Operation(summary = "文件上传")
  @PostMapping("/upload")
  @ResponseBody
  public Result<String> upload(@RequestPart("file") MultipartFile multipartFile) {
    try {

      String srcName = multipartFile.getOriginalFilename();
      String suffix = srcName.substring(srcName.lastIndexOf(ChartEnum.POINT.getCode()) + 1);
//      String fileUrl = "/filetype/" + IdUtil.getSnowflakeNextIdStr() + "." + suffix;
      String fileUrl = "/filetype/" + srcName;

      service.uploadFile(multipartFile, fileUrl);

      return Result.success(fileUrl);

    } catch (Exception e) {
      log.error("文件上传失败", e);
      return Result.error("文件上传失败");
    }
  }


}
