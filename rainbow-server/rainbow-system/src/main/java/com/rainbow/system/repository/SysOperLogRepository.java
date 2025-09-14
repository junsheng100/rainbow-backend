package com.rainbow.system.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.system.entity.SysOperLog;
import com.rainbow.system.model.vo.LogParamVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

  @Query("select t from SysOperLog t   where  1=1 " +
          "   and (:#{#data.keyword} is null " +
          "     or LOWER(t.title) like concat('%',LOWER(:#{#data.keyword}),'%')" +
          "     or LOWER(t.method) like concat('%',LOWER(:#{#data.keyword}),'%')" +
          "     or LOWER(t.requestMethod) like concat('%',LOWER(:#{#data.keyword}),'%')" +
          "     or LOWER(t.operatorType) like concat('%',LOWER(:#{#data.keyword}),'%')" +
          "     or LOWER(t.operName) like concat('%',LOWER(:#{#data.keyword}),'%')" +
          "     or LOWER(t.browser) like concat('%',LOWER(:#{#data.keyword}),'%')" +
          "     or LOWER(t.operUrl) like concat('%',LOWER(:#{#data.keyword}),'%')" +
          "     or LOWER(t.beanName) like concat('%',LOWER(:#{#data.keyword}),'%')" +
          "     or LOWER(t.beanName) like concat('%',LOWER(:#{#data.keyword}),'%')" +
          "     or LOWER(t.operIp) like concat('%',LOWER(:#{#data.keyword}),'%')" +
          "     or LOWER(t.operLocation) like concat('%',LOWER(:#{#data.keyword}),'%')" +
          "      )" +
          "   and (:#{#data.startTime} is null or t.operTime >= :#{#data.startTime}) " +
          "   and (:#{#data.endTime} is null or t.operTime <= :#{#data.endTime}) " )
  Page<SysOperLog> findPageList(@Param("data") LogParamVo data, Pageable pageable);
}
