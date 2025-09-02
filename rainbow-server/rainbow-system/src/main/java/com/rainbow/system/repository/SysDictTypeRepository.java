package com.rainbow.system.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.system.entity.SysDictType;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysDictTypeRepository extends BaseRepository<SysDictType,Long> {

  @Query("select t from SysDictType t where t.dictId in (?1) ")
  List<SysDictType> findInDictId(List<Long> idList);

  @Query("select t from SysDictType t where t.dictType = ?1 ")
  SysDictType findByDictType(String dictType);

  @Query("select t from SysDictType t where t.dictType in (?1) ")
  List<SysDictType> findInDictType(List<String> dictTypeList);


}
