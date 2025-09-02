package com.rainbow.scheduler.service;

import com.rainbow.base.model.vo.TaskLogVo;
import com.rainbow.base.service.BaseService;
import com.rainbow.scheduler.entity.TaskLog;

import java.util.Date;

/**
 * 任务日志服务接口
 */
public interface TaskLogService extends BaseService<TaskLog, String> {


  /**
   * 清理指定时间之前的日志
   */
  void cleanLogs(Date beforeTime);

  /**
   * 导出日志
   */
  byte[] exportLogs(Date startTime, Date endTime);


  TaskLog receive(TaskLogVo vo);
}