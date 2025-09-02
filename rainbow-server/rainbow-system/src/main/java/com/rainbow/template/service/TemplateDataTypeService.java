package com.rainbow.template.service;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.service.BaseService;
import com.rainbow.template.entity.TemplateDataType;

import java.util.List;

public interface TemplateDataTypeService extends BaseService<TemplateDataType,String> {


  PageData<TemplateDataType> pageList(CommonVo<String> vo);

  List<TemplateDataType> findAll();

  List<String> findDbGroup();

  List<TemplateDataType> findByDbType(String dbType);

  Boolean readFile(String fileUrl);
}
