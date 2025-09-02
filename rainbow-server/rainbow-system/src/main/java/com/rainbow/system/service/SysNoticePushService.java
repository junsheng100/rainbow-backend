package com.rainbow.system.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.system.entity.SysNoticePush;
import com.rainbow.system.model.dto.NoticePushDto;

import javax.validation.Valid;

public interface SysNoticePushService extends BaseService<SysNoticePush,String> {
  Boolean savePlanData(@Valid NoticePushDto vo);

  Integer getWillReadCount();
}
