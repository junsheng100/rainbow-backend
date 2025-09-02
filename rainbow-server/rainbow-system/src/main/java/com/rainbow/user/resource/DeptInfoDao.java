package com.rainbow.user.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.user.entity.DeptInfo;

import java.util.List;
import java.util.Map;

public interface DeptInfoDao extends BaseDao<DeptInfo,Long> {

  List<DeptInfo> findInId(List<Long> idList);

  List<DeptInfo> findAll();

  List<DeptInfo> findDeptInfoAll();

  Map<Long, String> findDeptName(List<Long> deptIdList);

  List<String> findUserIdByDeptId(Integer type, List<Long> deptIds);

  void getDeptIdList(List<DeptInfo> deptInfoList, List<Long> deptIdList);

 }
