package com.rainbow.system.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.system.entity.SysConfig;
import org.springframework.data.jpa.repository.Query;

public interface SysConfigRepository extends BaseRepository<SysConfig, Long> {

  @Query("select t from SysConfig t where trim(t.configKey) = trim(?1) ")
  SysConfig findByConfigKey(String configKey);

  @Query("select t.configValue from SysConfig t where trim(t.configKey)  = trim(?1) ")
  String findValueByKey(String configKey);
}
