package com.rainbow.system.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.system.entity.SysConfig;

public interface SysConfigDao extends BaseDao<SysConfig,Long> {

  SysConfig findByKey(String key);

  String getFileBasePath();
}
