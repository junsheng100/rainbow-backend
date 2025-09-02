package com.rainbow.template.controller;

import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.annotation.RestResponse;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.template.entity.TemplateDataType;
import com.rainbow.template.enums.JavaDataType;
import com.rainbow.template.service.TemplateDataTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/template/datatype")
@Tag(name = "模板/类型")
public class TemplateDataTypeController extends BaseController<TemplateDataType, String, TemplateDataTypeService> {

  @OperLog
  @RestResponse
  @PostMapping("/upload")
  @Operation(description = "文件上传")
  public Result<Boolean> readFile(@RequestPart(name = "file") MultipartFile multipartFile) {
    File file = null;
      try {
        String fileUrl = System.getProperty("user.dir") +File.separator+"temp"+File.separator+ multipartFile.getOriginalFilename();
        file = new File(fileUrl);
        File fdir = file.getParentFile();
        if (!fdir.isDirectory())
          fdir.mkdirs();

        multipartFile.transferTo(file);
        Boolean result = service.readFile(fileUrl);
        return Result.success(result);
      }catch (Exception e){
        e.printStackTrace();
        return Result.error(e.getMessage());
      }finally {
          if(null != file)
            file.delete();
      }
  }

  @RestResponse
  @Operation(description = "关键词分页查询")
  @PostMapping("/page/list")
  public Result<PageData<TemplateDataType>> pageList(@RequestBody CommonVo<String> vo) {
    PageData<TemplateDataType> page = service.pageList(vo);
    return Result.success(page);
  }


  @RestResponse
  @GetMapping("/all")
  @Operation(description = "查询所有")
  public Result<List<TemplateDataType>> findAll() {
    List<TemplateDataType> allList = service.findAll();
    return Result.success(allList);
  }

  @RestResponse
  @GetMapping("/db")
  @Operation(description = "查询DB分组")
  public Result<List<String>> findDbGroup() {
    List<String> allList = service.findDbGroup();
    return Result.success(allList);
  }

  @RestResponse
  @GetMapping("/java")
  @Operation(description = "查询Java分类")
  public Result<List<Map<String, Object>>> findJavaType() {
    List<Map<String, Object>> allList = JavaDataType.toList();
    return Result.success(allList);
  }


}
