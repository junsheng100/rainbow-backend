package com.rainbow.scheduler.resource.impl;

import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.scheduler.entity.TaskLog;
import com.rainbow.scheduler.repository.TaskLogRepository;
import com.rainbow.scheduler.resource.TaskLogDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskLogDaoImpl extends BaseDaoImpl<TaskLog,String, TaskLogRepository> implements TaskLogDao {
}
