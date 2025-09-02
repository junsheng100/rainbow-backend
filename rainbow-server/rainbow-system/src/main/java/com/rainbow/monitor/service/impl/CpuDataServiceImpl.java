package com.rainbow.monitor.service.impl;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.monitor.entity.CpuData;
import com.rainbow.monitor.model.vo.DataVo;
import com.rainbow.monitor.resource.CpuDataDao;
import com.rainbow.monitor.service.CpuDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CpuDataServiceImpl extends BaseServiceImpl<CpuData,String, CpuDataDao> implements CpuDataService {

  @Override
  public PageData<CpuData> pageData(CommonVo<DataVo> vo) {

    return baseDao.pageData(vo);
  }
}
