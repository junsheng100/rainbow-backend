package com.rainbow.user.service.impl;

import com.rainbow.base.constant.DataConstant;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.user.entity.SysMenu;
import com.rainbow.user.entity.SysRole;
import com.rainbow.user.entity.SysRoleMenu;
import com.rainbow.user.model.RoleTree;
import com.rainbow.user.resource.SysMenuDao;
import com.rainbow.user.resource.SysRoleDao;
import com.rainbow.user.resource.SysRoleMenuDao;
import com.rainbow.user.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole,Long, SysRoleDao> implements SysRoleService {


  @Autowired
  private SysMenuDao menuDao;

  @Autowired
  private SysRoleMenuDao roleMenuDao;

  @Override
  public List<RoleTree> findListTree(Long... roleId) {

    List<SysMenu> menuList = menuDao.findAll();
    List<SysRoleMenu> roleMenuList = roleMenuDao.findInRoleId(roleId);

    List<RoleTree> treeList = new ArrayList<>();

    for (SysMenu menu : menuList) {
      RoleTree data = convertRoleTree(menu);
      if (CollectionUtils.isNotEmpty(roleMenuList)) {
        List<Long> roleIdList =  roleMenuList.stream().filter(a -> a.getMenuId().equals(menu.getMenuId())).map(SysRoleMenu::getRoleId).collect(Collectors.toList());
        data.setRoleId(roleIdList);
        data.setChecked(CollectionUtils.isNotEmpty(roleIdList));
      }
      treeList.add(data);
    }

    for(RoleTree data:treeList){
      List<RoleTree> children = treeList.stream().filter(a->data.getId().equals(a.getParentId())).collect(Collectors.toList());
      data.setChildren(children);
    }

    treeList = treeList.stream().filter(t-> DataConstant.MENU_ROOT.equals(t.getParentId())).collect(Collectors.toList());

    return treeList;
  }


  @Override
  public SysRole store(@Valid SysRole entity) {
      baseDao.store(entity);
      if(CollectionUtils.isNotEmpty(entity.getRightIdList())){
        roleMenuDao.saveRoleRightList(entity.getRoleId(),entity.getRightIdList());

      }
      return entity;
  }

  private RoleTree convertRoleTree(SysMenu menu) {
    RoleTree data = new RoleTree();
    data.setId(menu.getMenuId());
    data.setLabel(menu.getMenuName());
    data.setParentId(menu.getParentId());
    data.setOrderNo(menu.getOrderNum());

    return data;
  }
}
