package com.rainbow.template.resource;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.resource.BaseDao;
import com.rainbow.template.entity.TemplateDataType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TemplateDataTypeDao extends BaseDao<TemplateDataType, String> {

  PageData<TemplateDataType> findPageList(String data, Pageable pageable);

  List<TemplateDataType> findAll();

  List<String> findDbGroup();

  List<TemplateDataType> findByDbType(String dbType);

  Integer getMaxOrderNum();

  TemplateDataType findType(String dbType, String columnType);
}
