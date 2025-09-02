package com.rainbow.user.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.user.entity.SysRole;

import java.util.List;

public interface SysRoleDao extends BaseDao<SysRole,Long> {
  List<SysRole> findInId(List<Long> roleIdList);
}
