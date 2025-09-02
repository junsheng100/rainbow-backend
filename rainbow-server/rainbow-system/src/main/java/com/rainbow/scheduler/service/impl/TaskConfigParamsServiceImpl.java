package com.rainbow.scheduler.service.impl;

import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.scheduler.entity.TaskConfigParams;
import com.rainbow.scheduler.resource.TaskConfigParamsDao;
import com.rainbow.scheduler.service.TaskConfigParamsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.scheduler.service.impl
 * @Filename：TaskConfigParamsServiceImpl
 * @Describe:
 */
@Slf4j
@Service
public class TaskConfigParamsServiceImpl extends BaseServiceImpl<TaskConfigParams,String, TaskConfigParamsDao> implements TaskConfigParamsService {


  @Override
  public List<TaskConfigParams> findByConfigId(String configId) {
    return StringUtils.isBlank(configId)? Collections.emptyList():super.baseDao.findByConfigId(configId);
  }


}
