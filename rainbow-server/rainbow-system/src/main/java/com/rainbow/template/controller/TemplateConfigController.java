package com.rainbow.template.controller;

import com.rainbow.base.annotation.RestResponse;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.template.entity.TemplateConfig;
import com.rainbow.template.service.TemplateConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/template/config")
@Tag(name = "模板/配置")
public class TemplateConfigController extends BaseController<TemplateConfig,String, TemplateConfigService> {

  @RestResponse
  @PostMapping("/data")
  @Operation(description = "查询模板列表")
  public Result<List<TemplateConfig>> findDataList() {
    return Result.success(service.findDataList());
  }

  @RestResponse
  @Operation(description = "关键词分页查询")
  @PostMapping("/page/list")
  public Result<PageData<TemplateConfig>> pageList(@RequestBody CommonVo<String> vo) {
    PageData<TemplateConfig> page = service.pageList(vo);
    return Result.success(page);
  }


}
