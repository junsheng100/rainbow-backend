package com.rainbow.scheduler.resource.impl;

import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.scheduler.entity.TaskConfig;
import com.rainbow.scheduler.repository.TaskConfigRepository;
import com.rainbow.scheduler.resource.TaskConfigDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskConfigDaoImpl extends BaseDaoImpl<TaskConfig,String, TaskConfigRepository> implements TaskConfigDao {


  @Override
  public TaskConfig findByTaskName(String taskName) {
    return StringUtils.isBlank(taskName) ? null : jpaRepository.findByTaskName(taskName);
  }


}
