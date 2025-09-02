package com.rainbow.template.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.template.entity.TemplateEntity;

import java.util.List;

public interface TemplateEntityService extends BaseService<TemplateEntity, String> {

  List<TemplateEntity> findAll();

}
