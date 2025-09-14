package com.rainbow.system.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.system.entity.SysConfig;

import java.util.List;

public interface SysConfigDao extends BaseDao<SysConfig,Long> {

  SysConfig findByKey(String key);

  String getFileBasePath();

  List<SysConfig> findConfigValue(String key);
}
