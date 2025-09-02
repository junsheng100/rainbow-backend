package com.rainbow.template.service;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.service.BaseService;
import com.rainbow.template.entity.TemplateConfig;

import java.util.List;

public interface TemplateConfigService extends BaseService<TemplateConfig, String> {


  List<TemplateConfig> findDataList();

  PageData<TemplateConfig> pageList(CommonVo<String> vo);
}
