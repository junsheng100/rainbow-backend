package com.rainbow.system.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.system.entity.SysDictData;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public interface SysDictDataDao extends BaseDao<SysDictData,Long> {


  List<SysDictData> findInDictType(List<String> dictTypeList);

  List<SysDictData> findByDictType(String dictType);
}
