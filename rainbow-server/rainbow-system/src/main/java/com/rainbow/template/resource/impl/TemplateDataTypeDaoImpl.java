package com.rainbow.template.resource.impl;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.template.entity.TemplateDataType;
import com.rainbow.template.repository.TemplateDataTypeRepository;
import com.rainbow.template.resource.TemplateDataTypeDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


@Slf4j
@Component
public class TemplateDataTypeDaoImpl extends BaseDaoImpl<TemplateDataType, String, TemplateDataTypeRepository> implements TemplateDataTypeDao {
  @Override
  public PageData<TemplateDataType> findPageList(String data, Pageable pageable) {
    Page<TemplateDataType> page = null;
    if (StringUtils.isBlank(data)) {
      page = jpaRepository.findAll(pageable);
    } else {
      page = jpaRepository.findPageList(data, pageable);
    }

    PageData<TemplateDataType> pageData = new PageData<>(page);
    return pageData;
  }

  @Override
  public List<TemplateDataType> findAll() {
    return jpaRepository.findAll();
  }

  @Override
  public List<String> findDbGroup() {
    return jpaRepository.findDbGroup();
  }

  @Override
  public List<TemplateDataType> findByDbType(String dbType) {
    return StringUtils.isBlank(dbType) ? Collections.emptyList() : jpaRepository.findByDbType(dbType);
  }

  @Override
  public Integer getMaxOrderNum() {
    Integer maxOrderNum = jpaRepository.getMaxOrderNum();
    return null == maxOrderNum ? 0 : maxOrderNum;
  }

  @Override
  public TemplateDataType findType(String dbType, String columnType) {
    if (StringUtils.isBlank(dbType) || StringUtils.isBlank(columnType))
      return null;
    return jpaRepository.findType(dbType, columnType);
  }
}
