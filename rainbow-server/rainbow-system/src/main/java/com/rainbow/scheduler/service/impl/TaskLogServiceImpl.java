package com.rainbow.scheduler.service.impl;

import com.rainbow.base.model.vo.TaskLogVo;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.base.utils.CommonUtils;
import com.rainbow.scheduler.entity.TaskLog;
import com.rainbow.scheduler.resource.TaskLogDao;
import com.rainbow.scheduler.service.TaskLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class TaskLogServiceImpl extends BaseServiceImpl<TaskLog, String, TaskLogDao> implements TaskLogService {


  @Override
  public void cleanLogs(Date beforeTime) {

  }

  @Override
  public byte[] exportLogs(Date startTime, Date endTime) {
    return new byte[0];
  }

  @Override
  public TaskLog receive(TaskLogVo vo) {

    TaskLog taskLog = new TaskLog();
    BeanUtils.copyProperties(vo, taskLog, CommonUtils.getNullPropertyNames(vo));
    super.baseDao.save(taskLog);
    return taskLog;
  }
}
