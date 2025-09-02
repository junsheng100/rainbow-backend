package com.rainbow.user.service;

import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.base.service.BaseService;
import com.rainbow.user.entity.DeptInfo;
import com.rainbow.user.model.UserDeptInfo;

import java.util.List;

public interface DeptInfoService extends BaseService<DeptInfo,Long> {

  List<DeptInfo> findTreeView(BaseVo<DeptInfo> vo);

  List<UserDeptInfo> findUserList(String pushType,List<Long> vo);
}
