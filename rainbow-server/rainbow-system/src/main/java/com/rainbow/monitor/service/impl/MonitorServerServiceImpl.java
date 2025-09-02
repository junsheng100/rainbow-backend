package com.rainbow.monitor.service.impl;

import com.rainbow.base.exception.BizException;
import com.rainbow.monitor.model.server.ServerInfo;
import com.rainbow.monitor.service.MonitorServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MonitorServerServiceImpl implements MonitorServerService {


  @Override
  public ServerInfo getServerInfo() {

    ServerInfo server = new ServerInfo();
    try {
      server.copyTo();
    } catch (Exception e) {
      throw new BizException(e.getMessage());
    }
    return server;

  }
}
