package com.rainbow.system.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.system.entity.SysLogin;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysLoginRepository extends BaseRepository<SysLogin, Long> {

  @Query("select t.country,t.pro,t.city,count(distinct t.userName) " +
          " from SysLogin t  group by t.country,t.pro,t.city ")
  List<Object[]> totalAreaPro();

  @Query("select t  from SysLogin t  where date_format(t.operTime,'%Y-%m-%d') = ?1 ")
  List<SysLogin> findTheDay(String theDay);

  @Query("select count(t.userName)  from SysLogin t  " +
          "where t.type = 'Login' " +
          "and  date_format(t.operTime,'%Y-%m-%d') = ?1 ")
  Long countLogin(String theDay);
}
