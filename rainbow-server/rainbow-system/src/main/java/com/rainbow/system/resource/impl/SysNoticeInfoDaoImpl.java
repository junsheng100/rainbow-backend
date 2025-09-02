package com.rainbow.system.resource.impl;

import com.rainbow.base.enums.InfoStage;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.system.entity.SysNoticeInfo;
import com.rainbow.system.repository.SysNoticeInfoRepository;
import com.rainbow.system.resource.SysNoticeInfoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SysNoticeInfoDaoImpl extends BaseDaoImpl<SysNoticeInfo, Long, SysNoticeInfoRepository> implements SysNoticeInfoDao {


  @Override
  public SysNoticeInfo check(SysNoticeInfo entity) {
    SysNoticeInfo old = getOne(entity);

    if (null == old) {
      if (null == entity.getStage())
        entity.setStage(InfoStage.WAIT);
    }
    return old;
  }



}
