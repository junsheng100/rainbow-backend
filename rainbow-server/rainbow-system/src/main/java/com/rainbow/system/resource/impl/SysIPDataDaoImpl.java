package com.rainbow.system.resource.impl;

import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.system.entity.SysIPData;
import com.rainbow.system.repository.SysIPDataRepository;
import com.rainbow.system.resource.SysIPDataDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.system.resource.impl
 * @Filename：SysIPAddressDaoImpl
 * @Describe:
 */
@Slf4j
@Component
public class SysIPDataDaoImpl extends BaseDaoImpl<SysIPData,String, SysIPDataRepository> implements SysIPDataDao {

  @Override
  public SysIPData getByIpaddr(String ip) {
    return StringUtils.isBlank(ip)?null:jpaRepository.getByIpaddr(ip);
  }
}
