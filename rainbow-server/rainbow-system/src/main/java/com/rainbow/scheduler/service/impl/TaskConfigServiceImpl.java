package com.rainbow.scheduler.service.impl;

import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.scheduler.entity.TaskConfig;
import com.rainbow.scheduler.job.LocalJobExecutor;
import com.rainbow.scheduler.resource.TaskConfigDao;
import com.rainbow.scheduler.resource.TaskConfigParamsDao;
import com.rainbow.scheduler.service.TaskConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

/**
 * 任务配置服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskConfigServiceImpl extends BaseServiceImpl<TaskConfig, String, TaskConfigDao> implements TaskConfigService {

  @Autowired
  private Scheduler scheduler;

  @Autowired
  private TaskConfigParamsDao paramsDao;


  @Override
  public TaskConfig store(@Valid TaskConfig entity) {
    try {
      if (validate(entity)) {
        baseDao.store(entity);
        if (CollectionUtils.isNotEmpty(entity.getParams())) {
          paramsDao.saveConfig(entity);
        }
        return entity;
      }
      throw new BizException("数据验证失败");
    }catch (Exception e){
      log.error(e.getMessage());
      throw new BizException("数据验证失败");
    }
  }


  @Override
  public TaskConfig save(@Valid TaskConfig entity) {
    throw new BizException("方法已失效");
  }


  @Override
  public void startTask(String id) {
    TaskConfig taskConfig = get(id);

    startTask(taskConfig);
  }

  @Override
  public void startTask(TaskConfig taskConfig) {

    try {
      if(null == taskConfig)
        return;

      JobDetail jobDetail = JobBuilder.newJob(LocalJobExecutor.class)
              .withIdentity(taskConfig.getTaskName())
              .build();

      CronTrigger trigger = TriggerBuilder.newTrigger()
              .withIdentity(taskConfig.getTaskName() + "_trigger")
              .withSchedule(CronScheduleBuilder.cronSchedule(taskConfig.getCronExpression()))
              .build();

      scheduler.scheduleJob(jobDetail, trigger);

      // 更新任务状态为运行中
      taskConfig.setStatus(UseStatus.NO.getCode());
      baseDao.save(taskConfig);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "启动任务失败: " + e.getMessage());
    }
  }

  @Override
  public void stopTask(String id) {
    TaskConfig taskConfig = get(id);

    try {
      scheduler.deleteJob(JobKey.jobKey(taskConfig.getTaskName()));

      // 更新任务状态为停止
      taskConfig.setStatus(UseStatus.DEL.getCode());
      baseDao.save(taskConfig);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "停止任务失败: " + e.getMessage());
    }
  }


  @Override
  public Boolean delete(String id) {
    return baseDao.remove(id);
  }

  @Override
  public Boolean deleteInBatch(List<String> data) {
    return baseDao.removeInBatch(data);
  }


  @Override
  public void executeOnce(String id) {
    TaskConfig taskConfig = get(id);

    try {
      JobDetail jobDetail = JobBuilder.newJob(LocalJobExecutor.class)
              .withIdentity(taskConfig.getTaskName() + "_once")
              .build();

      Trigger trigger = TriggerBuilder.newTrigger()
              .withIdentity(taskConfig.getTaskName() + "_trigger_once")
              .startNow()
              .build();

      scheduler.scheduleJob(jobDetail, trigger);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "执行任务失败: " + e.getMessage());
    }
  }

  @Override
  public boolean validateCronExpression(String cronExpression) {
    try {
      CronScheduleBuilder.cronSchedule(cronExpression);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public TaskConfig findByTaskName(String taskName) {
    return baseDao.findByTaskName(taskName);
  }


  public boolean validate(@Valid TaskConfig entity) {
    // 验证cron表达式
    if (!validateCronExpression(entity.getCronExpression())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "无效的cron表达式");
    }
    return true;
  }

} 