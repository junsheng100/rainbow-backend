package com.rainbow.monitor.resource.impl;

import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.monitor.entity.SysData;
import com.rainbow.monitor.repository.SysDataRepository;
import com.rainbow.monitor.resource.SysDataDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class SysDataDaoImpl extends BaseDaoImpl<SysData,String, SysDataRepository> implements SysDataDao {

  @Override
  public void cleanAll() {
    jpaRepository.deleteAllInBatch();
  }
}
