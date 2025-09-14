package com.rainbow.system.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.system.entity.SysLogin;
import com.rainbow.system.model.vo.LogParamVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface SysLoginRepository extends BaseRepository<SysLogin, Long> {

  @Query("select t.country,t.pro,t.city,count(distinct t.userName) " +
          " from SysLogin t  group by t.country,t.pro,t.city ")
  List<Object[]> totalAreaPro();

  @Query("select t  from SysLogin t  where date_format(t.operTime,'%Y-%m-%d') = ?1 ")
  List<SysLogin> findTheDay(String theDay);

  @Query("select count(distinct t.userName)  from SysLogin t  " +
          "where t.type = 'Login' " +
          "and  date_format(t.operTime,'%Y-%m-%d') = ?1 ")
  Long countLogin(String theDay);


@Query("select t from SysLogin t where 1=1 " +
        "   and ( ?1 is null " +
        "     or t.type like concat('%',?1,'%')" +
        "     or t.userName like concat('%',?1,'%')" +
        "     or t.country like concat('%',?1,'%')" +
        "     or t.city like concat('%',?1,'%')" +
        "     or t.pro like concat('%',?1,'%')" +
        "     or t.ipaddr like concat('%',?1,'%')" +
        "     or t.loginLocation like concat('%',?1,'%')" +
        "     or t.browser like concat('%',?1,'%')" +
        "     or t.os like concat('%',?1,'%')" +
        "      )" +
        "   and (?2 is null or t.operTime >= ?2 ) " +
        "   and (?3 is null or t.operTime <= ?3 )" )
  Page<SysLogin> findPageList(String keyword, Date startTime, Date endTime, Pageable pageable);



}
