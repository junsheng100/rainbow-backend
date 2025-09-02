package com.rainbow.monitor.resource;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.resource.BaseDao;
import com.rainbow.monitor.entity.JvmData;
import com.rainbow.monitor.model.server.ServerInfo;
import com.rainbow.monitor.model.vo.DataVo;

import java.util.Date;
import java.util.List;

public interface JvmDataDao extends BaseDao<JvmData,String> {

  JvmData saveServerInfo(String sysId, ServerInfo server, Date takeTime);

  boolean removeInSysId(List<String> sysIdList);

  void cleanAll();

  PageData<JvmData> pageData(CommonVo<DataVo> vo);
}
