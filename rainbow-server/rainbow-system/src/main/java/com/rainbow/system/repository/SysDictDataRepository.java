package com.rainbow.system.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.system.entity.SysDictData;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysDictDataRepository extends BaseRepository<SysDictData,Long> {

  @Query("select t from SysDictData t where t.dictType in (?1) ")
  List<SysDictData> findInDictType(List<String> dictTypeList);

  @Query("select t from SysDictData t where t.dictType = ?1 ")
  List<SysDictData> findByDictType(String dictType);

}
