package com.rainbow.system.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.system.entity.SysDictType;

public interface SysDictTypeService extends BaseService<SysDictType,Long> {

  void resetDictCache();

  void loadingDictCache();

  Boolean deleteIdList(Long... id);

  Boolean changeDisabled(Long dictId, String status);
}
