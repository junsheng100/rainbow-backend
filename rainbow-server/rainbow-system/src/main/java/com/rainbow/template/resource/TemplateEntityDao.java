package com.rainbow.template.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.template.entity.TemplateEntity;

import java.util.List;

public interface TemplateEntityDao extends BaseDao<TemplateEntity,String> {


  List<TemplateEntity> findInId(List<String> entityIdList);


  List<TemplateEntity> findAll();
}
