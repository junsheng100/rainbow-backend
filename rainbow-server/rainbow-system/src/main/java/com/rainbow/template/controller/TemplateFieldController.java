package com.rainbow.template.controller;

import com.rainbow.base.annotation.RestResponse;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.template.entity.TemplateDataType;
import com.rainbow.template.entity.TemplateEntity;
import com.rainbow.template.entity.TemplateField;
import com.rainbow.template.service.TemplateDataTypeService;
import com.rainbow.template.service.TemplateEntityService;
import com.rainbow.template.service.TemplateFieldService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/template/field")
@Tag(name = "模板/属性")
public class TemplateFieldController extends BaseController<TemplateField, String, TemplateFieldService> {

  @Autowired
  private TemplateEntityService entityService;

  @Autowired
  private TemplateDataTypeService dataTypeService;


  @RestResponse
  @GetMapping("/type/{entityId}")
  @Operation(description = "按实体 ID 查询")
  public Result<List<TemplateDataType>> findDataTypeByEntityId(@PathVariable(name = "entityId") String entityId) {

    TemplateEntity entity = entityService.get(entityId);
    if (null == entity || StringUtils.isBlank(entity.getDbType()))
      throw new BizException("数据不存在");
    if (null == entity || StringUtils.isBlank(entity.getDbType()))
      throw new BizException("未设置数据库类型");

    List<TemplateDataType> allList = dataTypeService.findByDbType(entity.getDbType());

    return Result.success(allList);
  }


}
