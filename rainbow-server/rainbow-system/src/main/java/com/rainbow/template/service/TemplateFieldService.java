package com.rainbow.template.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.template.entity.TemplateField;

import java.util.List;


public interface TemplateFieldService extends BaseService<TemplateField, String> {


  List<TemplateField> findByEntityId(String entityId);
}
