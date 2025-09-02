package com.rainbow.system.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.system.entity.SysConfig;

public interface SysConfigService extends BaseService<SysConfig,Long> {


  SysConfig getConfigValue(String configKey);
}
