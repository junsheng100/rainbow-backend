package com.rainbow.monitor.service.impl;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.monitor.entity.DiskData;
import com.rainbow.monitor.model.vo.DataVo;
import com.rainbow.monitor.resource.DiskDataDao;
import com.rainbow.monitor.service.DiskDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DiskDataServiceImpl extends BaseServiceImpl<DiskData,String, DiskDataDao> implements DiskDataService {

  @Override
  public PageData<DiskData> pageData(CommonVo<DataVo> vo) {
    return baseDao.pageData(vo);
  }

}
