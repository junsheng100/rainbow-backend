package com.rainbow.system.resource.impl;

import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.DateTools;
import com.rainbow.system.entity.SysOperLog;
import com.rainbow.system.repository.SysOperLogRepository;
import com.rainbow.system.resource.SysOperLogDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SysOperLogDaoImpl extends BaseDaoImpl<SysOperLog, Long, SysOperLogRepository> implements SysOperLogDao {
  @Override
  public Boolean deleteAll() {
    jpaRepository.deleteAll();
    return true;
  }

  @Override
  public List<Object[]> totalOperTopList() {

    List<Object[]> list = jpaRepository.totalOperTopList();

    return list;
  }

  @Override
  public List<Object[]> totalMonthList(Date start, Date end) {
    if (null == start || null == end)
      return null;
    SimpleDateFormat sdf = new SimpleDateFormat(DateTools.YYYY_MM);
    String startTime = sdf.format(start);
    String endTime = sdf.format(end);
    return jpaRepository.totalMonthList(startTime, endTime);
  }

  @Override
  public List<Object[]> totalUserList(Date start, Date end) {
    if (null == start || null == end)
      return null;
    SimpleDateFormat sdf = new SimpleDateFormat(DateTools.YYYY_MM_DD);
    String startTime = sdf.format(start);
    String endTime = sdf.format(end);
    return jpaRepository.totalUserList(startTime, endTime);
  }


}
