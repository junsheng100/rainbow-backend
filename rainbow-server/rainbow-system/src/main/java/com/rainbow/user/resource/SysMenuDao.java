package com.rainbow.user.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.user.entity.SysMenu;

import java.util.List;

public interface SysMenuDao extends BaseDao<SysMenu,Long> {
  List<SysMenu> findInMenuId(List<Long> menuIdLisr);

  List<SysMenu> findInMenuType(List<String> typeList);

  List<SysMenu> findByMenuType(String menuType);

  List<SysMenu> findAll();

  List<SysMenu> findChildren(Long menuId);

  Long countChildren(Long menuId);

  List<SysMenu> findMenuAll();

  List<SysMenu> findMenuInMenuId(List<Long> menuIdList);


  List<String> findInterfaceId();

  List<SysMenu> findInterfaceId(List<String> interfaceIdList);

  List<SysMenu> findNotInInterfaceId(List<String> interfaceIdList);

  List<String> findPermsByUserId(String userId);
}
