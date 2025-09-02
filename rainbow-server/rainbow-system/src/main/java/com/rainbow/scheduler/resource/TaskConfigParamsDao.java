package com.rainbow.scheduler.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.scheduler.entity.TaskConfig;
import com.rainbow.scheduler.entity.TaskConfigParams;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.scheduler.resource
 * @Filename：TaskConfigParamsDao
 * @Date：2025/8/16 18:47
 * @Describe:
 */
public interface TaskConfigParamsDao extends BaseDao<TaskConfigParams,String> {
  List<TaskConfigParams> findByConfigId(String configId);

  boolean saveConfig(@Valid TaskConfig entity);
}
