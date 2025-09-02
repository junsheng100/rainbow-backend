package com.rainbow.monitor.service;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.service.BaseService;
import com.rainbow.monitor.entity.CpuData;
import com.rainbow.monitor.model.vo.DataVo;

public interface CpuDataService extends BaseService<CpuData,String> {

  /**
   * 分页查询
   * @param vo
   * @return
   */
  PageData<CpuData> pageData(CommonVo<DataVo> vo);
}
