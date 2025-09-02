package com.rainbow.template.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.template.entity.TemplateField;

import java.util.List;

public interface TemplateFieldDao extends BaseDao<TemplateField,String> {

  List<TemplateField> findByEntityId(String entityId);

  List<TemplateField> findInEntityId(List<String> entityIdList);

  Boolean removeByEntityId(String id);

  Boolean removeInEntityId(List<String> data);
}
