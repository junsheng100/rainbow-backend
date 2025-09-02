package com.rainbow.monitor.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.monitor.entity.SysData;

public interface SysDataDao extends BaseDao<SysData,String> {
  void cleanAll();
}
