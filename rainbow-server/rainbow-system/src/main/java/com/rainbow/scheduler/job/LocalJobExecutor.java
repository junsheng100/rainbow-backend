package com.rainbow.scheduler.job;

import com.alibaba.fastjson2.JSON;
import com.rainbow.base.service.DynamicInvoker;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.scheduler.entity.TaskConfig;
import com.rainbow.scheduler.entity.TaskConfigParams;
import com.rainbow.scheduler.resource.TaskConfigDao;
import com.rainbow.scheduler.resource.TaskConfigParamsDao;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.scheduler.job
 * @Filename：LocalJobExecutor
 * @Describe:
 */
@Component
@RequiredArgsConstructor
public class LocalJobExecutor implements Job {

  @Autowired
  private TaskConfigDao taskConfigDao;

  private TaskConfigParamsDao taskConfigParamsDao;

  @Autowired
  private DynamicInvoker dynamicInvoker;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    String taskName = context.getJobDetail().getKey().getName();

    TaskConfig taskConfig = taskConfigDao.findByTaskName(taskName);
    String configId = taskConfig.getId();
    String beanName = taskConfig.getBeanName();
    String methodName = taskConfig.getMethodName();

    List<TaskConfigParams> paramsList = taskConfigParamsDao.findByConfigId(configId);

    Object[] args = getParamList(paramsList);
    /// 执行方法
    dynamicInvoker.invoke(beanName, methodName, args);

  }

  private Object[] getParamList(List<TaskConfigParams> list) {
    if (CollectionUtils.isEmpty(list))
      return null;
    Object[] args = new Object[list.size()];

    for (int i = 0; i < list.size(); i++) {
      TaskConfigParams data = list.get(i);
      String params = data.getParams();

      if (StringUtils.isNotBlank(params)) {
         args[i] = getParamData(params);
      } else {
        args[i] = null;
      }

    }
    return args;
  }

  private Object getParamData(String params) {

    if(StringUtils.isBlank(params))
      return null;

    int type = StringUtils.isValidJson(params);

    switch (type) {
      case 0:
        return JSON.parse(params);
      case 1:
        return JSON.parseObject(params);
      case 2:
        return JSON.parseArray(params);
      default:
        return params;
    }
  }


}
