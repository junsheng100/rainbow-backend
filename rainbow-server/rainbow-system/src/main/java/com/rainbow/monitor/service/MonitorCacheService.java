package com.rainbow.monitor.service;

import com.rainbow.monitor.model.dto.CacheTree;

import java.util.List;
import java.util.Map;

public interface MonitorCacheService {

  Map<String, Object> getInfo();

  List<CacheTree> getTreeInfo();
}
