package com.rainbow.system.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.system.entity.SysDictType;

import java.util.List;

public interface SysDictTypeDao extends BaseDao<SysDictType,Long> {
  List<SysDictType> findInDictId(List<Long> IdList);

  SysDictType findByDictType(String dictType);

  List<SysDictType> findInDictType(List<String> dictTypeList);
}
