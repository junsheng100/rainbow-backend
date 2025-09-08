package com.rainbow.aspect;

import com.rainbow.base.annotation.JobTask;
import com.rainbow.base.client.JobTaskClient;
import com.rainbow.base.exception.BaseException;
import com.rainbow.base.model.vo.TaskLogVo;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.scheduler.service.TaskLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.base.aspect
 * @Filename：JobTaskAspect
 * @Describe:
 */
@Slf4j
public class JobTaskAspect {

  @Autowired
  private TaskLogService taskLogService;

  @ResponseBody
  @Around("execute()")
  public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    TaskLogVo vo = getTaskLogVo(joinPoint);
    try {

      /// /////////////////////////////
      Object data = joinPoint.proceed();
      /// /////////////////////////////
      return data;
    } catch (BaseException e) {
      e.printStackTrace();
      log.error(e.getMessage());
      throw e;
    } finally {
      saveLogMess(vo);
    }

  }


  private void saveLogMess(TaskLogVo vo) {
    setEndTime(vo);
    taskLogService.receive(vo);
  }

  private void setEndTime(TaskLogVo vo) {
    if (null != vo) {
      vo.setEndTime(new Date());
    }
  }

  private TaskLogVo getTaskLogVo(ProceedingJoinPoint joinPoint) {

    MethodSignature signature = (MethodSignature) joinPoint.getSignature();

    Method method = signature.getMethod();
    JobTask task = method.getAnnotation(JobTask.class);
    if (null != task) {
      TaskLogVo vo = new TaskLogVo();
      String taskName = StringUtils.isBlank(task.name()) ? task.value() : task.name();
      vo.setTaskName(taskName);
      Date date = new Date();
      vo.setStartTime(date);
      vo.setExecTime(date);
      return vo;
    }
    return null;
  }


  @Pointcut("@annotation(com.rainbow.base.annotation.JobTask)")
  public void execute() {

  }

}
