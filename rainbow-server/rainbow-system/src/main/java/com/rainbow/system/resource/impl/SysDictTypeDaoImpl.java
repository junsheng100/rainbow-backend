package com.rainbow.system.resource.impl;

import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.system.entity.SysDictType;
import com.rainbow.system.repository.SysDictTypeRepository;
import com.rainbow.system.resource.SysDictTypeDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SysDictTypeDaoImpl extends BaseDaoImpl<SysDictType, Long,SysDictTypeRepository> implements SysDictTypeDao {


  @Override
  public List<SysDictType> findInDictId(List<Long> IdList) {

    return super.jpaRepository.findInDictId(IdList);
  }

  @Override
  public SysDictType findByDictType(String dictType) {
    return StringUtils.isBlank(dictType)?null:jpaRepository.findByDictType(dictType);
  }

  @Override
  public List<SysDictType> findInDictType(List<String> dictTypeList) {
    return CollectionUtils.isEmpty(dictTypeList)?null:jpaRepository.findInDictType(dictTypeList);
  }
}
