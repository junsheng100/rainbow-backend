package com.rainbow.template.controller;

import com.rainbow.base.annotation.RestResponse;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.Result;
import com.rainbow.template.entity.TemplateEntity;
import com.rainbow.template.service.TemplateEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/template/entity")
@Tag(name = "模板/实体类")
public class TemplateEntityController extends BaseController<TemplateEntity,String, TemplateEntityService> {


  @RestResponse
  @PostMapping("/data")
  @Operation(description = "查询所有实体类模板")
  public Result<List<TemplateEntity>> findAll() {
    /// ///////////////////////////////////
    return Result.success(service.findAll());
  }



}
