package com.rainbow.monitor.service.impl;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.monitor.entity.JvmData;
import com.rainbow.monitor.model.vo.DataVo;
import com.rainbow.monitor.resource.JvmDataDao;
import com.rainbow.monitor.service.JvmDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JvmDataServiceImpl extends BaseServiceImpl<JvmData,String, JvmDataDao> implements JvmDataService {


  @Override
  public PageData<JvmData> pageData(CommonVo<DataVo> vo) {
    return baseDao.pageData(vo);
  }
}
