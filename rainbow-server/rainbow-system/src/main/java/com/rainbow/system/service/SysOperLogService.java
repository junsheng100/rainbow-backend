package com.rainbow.system.service;

import com.rainbow.base.enums.TimeType;
import com.rainbow.base.model.vo.OperLogVo;
import com.rainbow.base.service.BaseService;
import com.rainbow.system.entity.SysOperLog;
import com.rainbow.system.model.vo.OperLogData;
import com.rainbow.system.model.vo.OperLogMonthData;
import com.rainbow.system.model.vo.OperLogUserData;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

public interface SysOperLogService extends BaseService<SysOperLog,Long> {

  Boolean receive(@Valid OperLogVo vo);

  Boolean cleanAll();

  List<OperLogData> totalOperTopList(Integer top,boolean desc);

  List<OperLogMonthData> totalMonthList(Date start,Date end,Integer top);

  List<OperLogUserData> totalUserList(Date start, Date end, TimeType type);
}
