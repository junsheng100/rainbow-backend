package com.rainbow.user.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.user.entity.SysRoleMenu;

import java.util.List;

public interface SysRoleMenuDao extends BaseDao<SysRoleMenu,Long> {
  List<SysRoleMenu> findByRoleId(Long roleId);

  List<SysRoleMenu> findInRoleId(Long... roleId);

  boolean saveRoleRightList(Long roleId, List<Long> rightIdList);

  List<SysRoleMenu> findInRoleIdList(List<Long> roleIdList);

  List<SysRoleMenu> findByMenuId(Long menuId);

  void removeList(Long menuId);


}
