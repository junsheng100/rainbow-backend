package com.rainbow.appdoc.controller;

import com.rainbow.appdoc.entity.AppInterface;
import com.rainbow.appdoc.service.ApiInterfaceService;
import com.rainbow.base.annotation.RestResponse;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app/interface")
@Tag(name = "API接口管理")
public class ApiInterfaceController extends BaseController<AppInterface, String, ApiInterfaceService> {


  @RestResponse
  @PostMapping("/category/{categoryId}")
  @Operation(description = "查询接口列表")
  public Result<List<AppInterface>> findByCategory(@PathVariable(name = "categoryId") String categoryId) {

    return Result.success(service.findByCategoryId(categoryId));
  }



}