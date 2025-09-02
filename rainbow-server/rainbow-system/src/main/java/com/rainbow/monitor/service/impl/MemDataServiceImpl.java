package com.rainbow.monitor.service.impl;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.monitor.entity.MemData;
import com.rainbow.monitor.model.vo.DataVo;
import com.rainbow.monitor.resource.MemDataDao;
import com.rainbow.monitor.service.MemDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MemDataServiceImpl extends BaseServiceImpl<MemData,String, MemDataDao> implements MemDataService {


  @Override
  public PageData<MemData> pageData(CommonVo<DataVo> vo) {
    return baseDao.pageData(vo);
  }
}
