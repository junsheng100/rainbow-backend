package com.rainbow.system.resource.impl;

import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.system.entity.SysLogin;
import com.rainbow.system.repository.SysLoginRepository;
import com.rainbow.system.resource.SysLoginDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SysLoginDaoImpl extends BaseDaoImpl<SysLogin, Long, SysLoginRepository> implements SysLoginDao {
  @Override
  public Boolean deleteAll() {
    jpaRepository.deleteAll();
    return true;
  }

  @Override
  public List<Object[]> totalAreaPro() {
    return jpaRepository.totalAreaPro();
  }

  @Override
  public List<SysLogin> findTheDay(String theDay) {
    if (StringUtils.isBlank(theDay))
      return null;

    return jpaRepository.findTheDay(theDay);
  }

  @Override
  public Long countLogin(String theDay) {
    if (StringUtils.isBlank(theDay))
      return 0L;
    Long count = jpaRepository.countLogin(theDay);
    count = null == count ? 0L : count;
    return count;
  }


}
