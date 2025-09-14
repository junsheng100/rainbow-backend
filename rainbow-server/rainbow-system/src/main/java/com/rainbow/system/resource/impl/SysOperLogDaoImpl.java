package com.rainbow.system.resource.impl;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.DateTools;
import com.rainbow.system.entity.SysOperLog;
import com.rainbow.system.model.vo.LogParamVo;
import com.rainbow.system.repository.SysOperLogRepository;
import com.rainbow.system.resource.SysOperLogDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @Override
  public PageData<SysOperLog> pageList(LogParamVo data, Pageable pageable) {
    Page<SysOperLog> page = jpaRepository.findPageList(data, pageable);

    PageData<SysOperLog> pageData = new PageData<>(page);
    return pageData;
  }


}
