package com.rainbow.template.resource.impl;

import com.rainbow.base.exception.DataException;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.template.entity.TemplateEntity;
import com.rainbow.template.repository.TemplateEntityRepository;
import com.rainbow.template.resource.TemplateEntityDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TemplateEntityDaoImpl extends BaseDaoImpl<TemplateEntity, String, TemplateEntityRepository> implements TemplateEntityDao {


  public TemplateEntity check(TemplateEntity entity) {
    TemplateEntity old = getOne(entity);

    if(null != old){
      if(StringUtils.isBlank(entity.getId())){
        throw new DataException("模板名称已存在");
      }
    }

    String entityName = entity.getEntityName();
    String tableName = entity.getTableName();
    tableName = StringUtils.isNotBlank(tableName) ?tableName.trim() : StringUtils.toUnderScoreCase(entityName);
    entity.setTableName(tableName);


    return old;
  }

  @Override
  public List<TemplateEntity> findInId(List<String> entityIdList) {
    return CollectionUtils.isEmpty(entityIdList)?null:jpaRepository.findAllById(entityIdList);
  }

  @Override
  public List<TemplateEntity> findAll() {
    return jpaRepository.findDataList();
  }
}
