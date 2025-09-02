package com.rainbow.system.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.system.entity.SysLogin;

import java.util.List;

public interface SysLoginDao extends BaseDao<SysLogin,Long> {
  Boolean deleteAll();


  List<Object[]> totalAreaPro();

  List<SysLogin> findTheDay(String theDay);

  Long countLogin(String theDay);
}
