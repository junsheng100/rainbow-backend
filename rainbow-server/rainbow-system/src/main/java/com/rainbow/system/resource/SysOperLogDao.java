package com.rainbow.system.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.system.entity.SysOperLog;

import java.util.Date;
import java.util.List;

public interface SysOperLogDao extends BaseDao<SysOperLog,Long> {
  Boolean deleteAll();

  List<Object[]> totalOperTopList();

  List<Object[]> totalMonthList(Date start, Date end);

  List<Object[]> totalUserList(Date start, Date end);
}
