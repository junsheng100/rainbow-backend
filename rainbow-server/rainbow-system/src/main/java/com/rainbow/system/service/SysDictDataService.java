package com.rainbow.system.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.system.entity.SysDictData;

import java.util.List;

public interface SysDictDataService extends BaseService<SysDictData,Long> {

  List<SysDictData> findByType(String type);
}
