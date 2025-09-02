package com.rainbow.scheduler.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.scheduler.entity.TaskConfigParams;

import java.util.List;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.scheduler.service
 * @Filename：TaskConfigParamsService
 * @Date：2025/8/16 18:49
 * @Describe:
 */
public interface TaskConfigParamsService extends BaseService<TaskConfigParams,String> {
  /**
  * 根据任务配置ID查询参数
  * @param configId
  * @return
  */
  List<TaskConfigParams> findByConfigId(String configId);
}
