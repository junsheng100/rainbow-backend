package com.rainbow.monitor.service;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.service.BaseService;
import com.rainbow.monitor.entity.MemData;
import com.rainbow.monitor.model.vo.DataVo;

public interface MemDataService extends BaseService<MemData, String> {


  PageData<MemData> pageData(CommonVo<DataVo> vo);


}
