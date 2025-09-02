package com.rainbow.system.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.system.entity.SysIPData;
import org.springframework.data.jpa.repository.Query;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.system.repository
 * @Filename：SysIPAddressRepository
 * @Describe:
 */
public interface SysIPDataRepository extends BaseRepository<SysIPData,String> {

  @Query("select t from SysIPData t where t.ipaddr = ?1  ")
  SysIPData getByIpaddr(String ip);
}
