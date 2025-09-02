package com.rainbow.monitor.service;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.service.BaseService;
import com.rainbow.monitor.entity.DiskData;
import com.rainbow.monitor.model.vo.DataVo;

public interface DiskDataService extends BaseService<DiskData,String> {

  PageData<DiskData> pageData(CommonVo<DataVo> vo);

}
