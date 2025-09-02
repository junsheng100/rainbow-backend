package com.rainbow.monitor.service.impl;

import com.rainbow.base.exception.BizException;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.monitor.entity.*;
import com.rainbow.monitor.model.server.ServerInfo;
import com.rainbow.monitor.model.server.Sys;
import com.rainbow.monitor.resource.*;
import com.rainbow.monitor.service.SysDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SysDataServiceImpl extends BaseServiceImpl<SysData, String, SysDataDao> implements SysDataService {

  @Autowired
  private CpuDataDao cpuDataDao;
  @Autowired
  private DiskDataDao diskDataDao;
  @Autowired
  private JvmDataDao jvmDataDao;
  @Autowired
  private MemDataDao memDataDao;

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

  @Transactional
  @Override
  public SysData saveServerInfo() {

    ServerInfo server = new ServerInfo();
    try {
      server.copyTo();
      Date takeTime = new Date();
      Sys sys = server.getSys();
      SysData entity = new SysData(sys);
      super.baseDao.save(entity);
      String sysId = entity.getId();
      /// /////////////////////////
      CpuData cpuData = cpuDataDao.saveServerInfo(sysId, server, takeTime);
      List<DiskData> diskData = diskDataDao.saveServerInfo(sysId, server, takeTime);
      JvmData jvmData = jvmDataDao.saveServerInfo(sysId, server, takeTime);
      MemData memData = memDataDao.saveServerInfo(sysId, server, takeTime);
      /// /////////////////////////
      entity.setCpuData(cpuData);
      entity.setJvmData(jvmData);
      entity.setMemData(memData);
      entity.setDiskData(diskData);
      /// /////////////////////////
      return entity;
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new BizException(e.getMessage());
    }
  }

  @Transactional
  public Boolean deleteInBatch(CommonVo<List<String>> vo) {
    if(null !=  vo.getData() && vo.getData().size() > 0){
      List<String> sysIdList = vo.getData();
      baseDao.removeInBatch(sysIdList);

      cpuDataDao.removeInSysId(sysIdList);
      diskDataDao.removeInSysId(sysIdList);
      jvmDataDao.removeInSysId(sysIdList);
      memDataDao.removeInSysId(sysIdList);

      return true;
    }
    return false;
  }

  @Override
  public Boolean cleanAll() {

    super.baseDao.cleanAll();
    cpuDataDao.cleanAll();
    diskDataDao.cleanAll();
    jvmDataDao.cleanAll();
    memDataDao.cleanAll();

    return true;
  }


}
