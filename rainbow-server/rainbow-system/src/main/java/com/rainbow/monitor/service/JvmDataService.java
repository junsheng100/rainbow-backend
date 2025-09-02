package com.rainbow.monitor.service;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.service.BaseService;
import com.rainbow.monitor.entity.JvmData;
import com.rainbow.monitor.model.vo.DataVo;

public interface JvmDataService extends BaseService<JvmData, String> {

  PageData<JvmData> pageData(CommonVo<DataVo> vo);

}
