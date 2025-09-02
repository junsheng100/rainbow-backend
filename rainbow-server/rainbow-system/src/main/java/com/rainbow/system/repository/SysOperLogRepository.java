package com.rainbow.system.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.system.entity.SysOperLog;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysOperLogRepository extends BaseRepository<SysOperLog, Long> {

  @Query("select t.beanName ,t.method ,t.title,count(t.id)  " +
          "from SysOperLog t group by t.beanName ,t.method,t.title ")
  List<Object[]> totalOperTopList();

  @Query(value = " select t.bean_name ,t.method ,date_format(t.oper_time,'%Y-%m') ,count(t.oper_id) cnt ,title\n" +
          "\tfrom sys_oper_log t \n" +
          " where date_format(t.oper_time,'%Y-%m')  BETWEEN ?1 and  ?2 \n " +
          "group by t.bean_name ,t.method ,date_format(t.oper_time,'%Y-%m'),t.title", nativeQuery = true)
  List<Object[]> totalMonthList(String startTime, String endTime);

  @Query(value = "select date_format(t.operTime,'%Y-%m-%d') ,\n" +
          "\t count(distinct t.operName ) ,\n" +
          "\t count(t.operId) \n" +
          "\t from SysOperLog t \n" +
          "\t where date_format(t.operTime,'%Y-%m-%d') BETWEEN ?1 and  ?2 \n " +
          "\t  group by  date_format(t.operTime,'%Y-%m-%d')")
  List<Object[]> totalUserList(String startTime, String endTime);
}
