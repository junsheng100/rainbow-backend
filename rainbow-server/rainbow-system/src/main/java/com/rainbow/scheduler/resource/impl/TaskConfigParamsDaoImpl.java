package com.rainbow.scheduler.resource.impl;

import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.scheduler.entity.TaskConfig;
import com.rainbow.scheduler.entity.TaskConfigParams;
import com.rainbow.scheduler.repository.TaskConfigParamsRepository;
import com.rainbow.scheduler.resource.TaskConfigParamsDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.scheduler.resource.impl
 * @Filename：TaskConfigParamsDaoImpl
 * @Describe:
 */
@Slf4j
@Component
public class TaskConfigParamsDaoImpl extends BaseDaoImpl<TaskConfigParams, String, TaskConfigParamsRepository> implements TaskConfigParamsDao {


  @Override
  public boolean saveConfig(TaskConfig entity) {

    if (null == entity || StringUtils.isBlank(entity.getId()))
      return false;
    if (CollectionUtils.isEmpty(entity.getParams()))
      return false;
    List<TaskConfigParams> oldList = findByConfigId(entity.getId());

    if(CollectionUtils.isNotEmpty(oldList)){
      super.jpaRepository.deleteAll(oldList);
    }

    for(TaskConfigParams data : entity.getParams()){
      data.setConfigId(entity.getId());
      jpaRepository.save(data);
    }

    return true;
  }


  @Override
  public List<TaskConfigParams> findByConfigId(String configId) {

    return StringUtils.isBlank(configId) ? null : super.jpaRepository.findByConfigId(configId);
  }


}
