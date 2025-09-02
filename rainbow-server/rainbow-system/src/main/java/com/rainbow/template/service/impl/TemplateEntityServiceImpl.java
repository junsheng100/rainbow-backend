package com.rainbow.template.service.impl;

import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.template.entity.TemplateEntity;
import com.rainbow.template.resource.TemplateEntityDao;
import com.rainbow.template.resource.TemplateFieldDao;
import com.rainbow.template.service.TemplateEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class TemplateEntityServiceImpl extends BaseServiceImpl<TemplateEntity,String, TemplateEntityDao> implements TemplateEntityService {

  @Autowired
  private TemplateFieldDao fieldDao;

  @Override
  public List<TemplateEntity> findAll() {
    return baseDao.findAll();
  }


  @Transactional
  @Override
  public Boolean delete(String id) {

    fieldDao.removeByEntityId(id);
    return baseDao.remove(id);
  }




  @Override
  public Boolean deleteInBatch(List<String> data) {
    return removeInBatch(data);
  }


  @Override
  public boolean removeInBatch(List<String> ids) {
    fieldDao.removeInEntityId(ids);
    return baseDao.removeInBatch(ids);
  }


}
