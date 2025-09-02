package com.rainbow.template.service.impl;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.resource.impl.DataManager;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.template.entity.TemplateConfig;
import com.rainbow.template.resource.TemplateConfigDao;
import com.rainbow.template.service.TemplateConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TemplateConfigServiceImpl extends BaseServiceImpl<TemplateConfig, String, TemplateConfigDao> implements TemplateConfigService {

  @Autowired
  protected DataManager<TemplateConfig> dataManager;

  @Override
  public List<TemplateConfig> findDataList() {
    return baseDao.findDataList();
  }

  @Override
  public PageData<TemplateConfig> pageList(CommonVo<String> vo) {
    Pageable pageable = dataManager.getCommonPageable(vo, TemplateConfig.class);
    PageData<TemplateConfig> pageData = baseDao.findPageList(vo.getData(), pageable);

    return pageData;
  }
}
