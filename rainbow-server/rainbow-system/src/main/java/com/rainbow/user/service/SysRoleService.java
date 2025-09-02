package com.rainbow.user.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.user.entity.SysRole;
import com.rainbow.user.model.RoleTree;

import java.util.List;

public interface SysRoleService extends BaseService<SysRole,Long> {

  List<RoleTree> findListTree(Long... roleId);

}
