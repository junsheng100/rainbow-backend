package com.rainbow.scheduler.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.scheduler.entity.TaskConfig;

public interface TaskConfigDao extends BaseDao<TaskConfig,String> {

  TaskConfig findByTaskName(String taskName);

 }
