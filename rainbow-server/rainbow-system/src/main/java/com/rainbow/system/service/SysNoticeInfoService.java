package com.rainbow.system.service;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.PageDomain;
import com.rainbow.base.service.BaseService;
import com.rainbow.system.entity.SysNoticeInfo;

public interface SysNoticeInfoService extends BaseService<SysNoticeInfo, Long> {
  SysNoticeInfo read(Long id);

  PageData<SysNoticeInfo> pageWill(PageDomain vo);
}
