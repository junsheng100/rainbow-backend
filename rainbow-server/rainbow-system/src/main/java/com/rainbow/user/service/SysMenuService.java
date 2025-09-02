package com.rainbow.user.service;

import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.base.service.BaseService;
import com.rainbow.user.entity.SysMenu;

import java.util.List;

public interface SysMenuService extends BaseService<SysMenu,Long> {

  List<SysMenu> findChildren(Long parentId);

  List<SysMenu> findTreeList(String menuType);


  List<SysMenu> findMenuTreeView(BaseVo<SysMenu> vo);


  List<SysMenu> findMenuTree();


}
