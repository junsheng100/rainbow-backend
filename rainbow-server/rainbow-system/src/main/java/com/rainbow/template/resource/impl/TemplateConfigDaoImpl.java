package com.rainbow.template.resource.impl;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.template.entity.TemplateConfig;
import com.rainbow.template.repository.TemplateConfigRepository;
import com.rainbow.template.resource.TemplateConfigDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TemplateConfigDaoImpl extends BaseDaoImpl<TemplateConfig,String, TemplateConfigRepository> implements TemplateConfigDao {

  @Override
  public List<TemplateConfig> findInId(List<String> configIdList) {
    return CollectionUtils.isEmpty(configIdList)?null:jpaRepository.findAllById(configIdList);
  }

  @Override
  public List<TemplateConfig> findDataList() {
    return super.jpaRepository.findDataList();
  }

  @Override
  public PageData<TemplateConfig> findPageList(String data, Pageable pageable) {
    Page<TemplateConfig> page = null;
    if (StringUtils.isBlank(data)) {
      page = jpaRepository.findAll(pageable);
    } else {
      page = jpaRepository.findPageList(data, pageable);
    }

    PageData<TemplateConfig> pageData = new PageData<>(page);
    return pageData;
  }
}
