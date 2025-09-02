package com.rainbow.template.resource.impl;

import com.rainbow.base.exception.DataException;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.template.entity.TemplateField;
import com.rainbow.template.repository.TemplateFieldRepository;
import com.rainbow.template.resource.TemplateFieldDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TemplateFieldDaoImpl extends BaseDaoImpl<TemplateField, String, TemplateFieldRepository> implements TemplateFieldDao {


  @Override
  public List<TemplateField> findByEntityId(String entityId) {
    return StringUtils.isBlank(entityId) ? null : jpaRepository.findByEntityId(entityId);
  }

  @Override
  public List<TemplateField> findInEntityId(List<String> entityIdList) {
    return CollectionUtils.isEmpty(entityIdList) ? null : jpaRepository.findInEntityId(entityIdList);
  }

  @Override
  public Boolean removeByEntityId(String entityId) {
    if (StringUtils.isBlank(entityId))
      throw new DataException("实例 ID 不能为空");
    jpaRepository.removeByEntityId(entityId);
    return true;
  }

  @Override
  public Boolean removeInEntityId(List<String> entityIdList) {
    if (CollectionUtils.isEmpty(entityIdList))
      throw new DataException("实例 ID 不能为空");
    jpaRepository.removeInEntityId(entityIdList);
    return true;
  }
}
