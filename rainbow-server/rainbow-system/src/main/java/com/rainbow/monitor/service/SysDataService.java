package com.rainbow.monitor.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.monitor.entity.SysData;
import com.rainbow.monitor.model.server.ServerInfo;

public interface SysDataService extends BaseService<SysData, String> {

  ServerInfo getServerInfo();

  SysData saveServerInfo();

  Boolean cleanAll();
}
