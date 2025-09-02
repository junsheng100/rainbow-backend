package com.rainbow.template.service.impl;

import com.rainbow.base.exception.BizException;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.template.entity.TemplateDataType;
import com.rainbow.template.entity.TemplateEntity;
import com.rainbow.template.entity.TemplateField;
import com.rainbow.template.resource.TemplateDataTypeDao;
import com.rainbow.template.resource.TemplateEntityDao;
import com.rainbow.template.resource.TemplateFieldDao;
import com.rainbow.template.service.TemplateFieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Service
public class TemplateFieldServiceImpl extends BaseServiceImpl<TemplateField,String, TemplateFieldDao> implements TemplateFieldService {


  @Autowired
  private TemplateDataTypeDao  dataTypeDao;
  @Autowired
  private TemplateEntityDao entityDao;

  @Override
  public List<TemplateField> findByEntityId(String entityId) {
    return baseDao.findByEntityId(entityId);
  }

  @Override
  public Boolean delete(String id) {
    return baseDao.remove(id);
  }

  public void handleData(@Valid TemplateField entity) {

    TemplateEntity data = entityDao.get(entity.getEntityId());

    String dbType = data.getDbType();
    String columnType = entity.getColumnType();
    TemplateDataType dataType = dataTypeDao.findType(dbType,columnType);

    if(null == dataType)
      throw new BizException("数据类型不存在");
    entity.setFieldType(dataType.getJavaType());

  }

}
