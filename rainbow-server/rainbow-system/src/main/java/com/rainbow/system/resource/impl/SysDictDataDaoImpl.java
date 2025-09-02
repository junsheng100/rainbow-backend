package com.rainbow.system.resource.impl;

import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.system.entity.SysDictData;
import com.rainbow.system.repository.SysDictDataRepository;
import com.rainbow.system.resource.SysDictDataDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SysDictDataDaoImpl extends BaseDaoImpl<SysDictData,Long,SysDictDataRepository> implements SysDictDataDao {


  @Override
  public List<SysDictData> findInDictType(List<String> dictTypeList) {
    return CollectionUtils.isEmpty(dictTypeList)?null:jpaRepository.findInDictType(dictTypeList);
  }

  @Override
  public List<SysDictData> findByDictType(String dictType) {
    return StringUtils.isBlank(dictType)?null:jpaRepository.findByDictType(dictType);
  }
}
