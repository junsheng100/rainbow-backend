package com.rainbow.appdoc.controller;

import com.rainbow.appdoc.entity.AppCategory;
import com.rainbow.appdoc.model.CategoryModel;
import com.rainbow.appdoc.service.ApiCategoryService;
import com.rainbow.base.annotation.NoRepeatSubmit;
import com.rainbow.base.annotation.RestResponse;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.vo.CommonVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/category")
@Tag(name = "API控制类管理")
public class ApiCategoryController extends BaseController<AppCategory, String, ApiCategoryService> {



  @RestResponse
  @PostMapping("/find")
  @Operation(description = "查询接口类列表")
  public Result<PageData<AppCategory>> findByKeyword(@RequestBody CommonVo<String> vo) {
    PageData<AppCategory> pageData = service.findByKeyword(vo);
    return Result.success(pageData);
  }


  @RestResponse
  @PostMapping("/menus")
  @Operation(description = "菜单接口类列表")
  public Result<PageData<CategoryModel>> findMenus(@RequestBody CommonVo<String> vo) {
    PageData<CategoryModel> pageData = service.findMenus(vo);
    return Result.success(pageData);
  }


  @NoRepeatSubmit
  @RestResponse
  @PostMapping("/refresh/{className}")
  @Operation(description = "刷新接口类信息")
  public Result<AppCategory> refresh(@PathVariable(name = "className") String className) {

    AppCategory data =  service.create(className);
    return Result.success(data);
  }


  @NoRepeatSubmit
  @RestResponse
  @PostMapping("/refresh/all")
  @Operation(description = "刷新所有接口")
  public Result<Boolean> refreshAll() {

    Boolean flag =  service.init();

    return Result.success(flag);
  }

} 