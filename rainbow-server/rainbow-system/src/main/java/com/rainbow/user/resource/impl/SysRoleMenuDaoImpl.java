package com.rainbow.user.resource.impl;

import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.user.entity.SysRoleMenu;
import com.rainbow.user.repository.SysRoleMenuRepository;
import com.rainbow.user.resource.SysRoleMenuDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class SysRoleMenuDaoImpl extends BaseDaoImpl<SysRoleMenu, Long,SysRoleMenuRepository> implements SysRoleMenuDao {

  @Override
  public List<SysRoleMenu> findByRoleId(Long roleId) {
    return null == roleId ? Collections.emptyList() : jpaRepository.findByRoleId(roleId);
  }

  @Override
  public List<SysRoleMenu> findInRoleId(Long... roleId) {
    return null == roleId ? Collections.emptyList() : jpaRepository.findInRoleId(Arrays.asList(roleId));
  }

  @Override
  public boolean saveRoleRightList(Long roleId, List<Long> rightIdList) {

    if (null == roleId || CollectionUtils.isEmpty(rightIdList))
      return false;

    List<SysRoleMenu> oldList = jpaRepository.findByRoleId(roleId);
    String userName = super.getUserName();
    LocalDateTime date = LocalDateTime.now();
    String status = UseStatus.NO.getCode();

    List<SysRoleMenu> rightList = new ArrayList<>();

    for(Long menuId:rightIdList){
      SysRoleMenu right = new SysRoleMenu(roleId, menuId,userName,date,userName,date,status);
      rightList.add(right);
    }

    if(CollectionUtils.isNotEmpty(oldList)){
      super.jpaRepository.deleteAll(oldList);
    }

    if(CollectionUtils.isNotEmpty(rightList)){
//      super.jpaRepository.saveAllAndFlush(rightList);
      for(SysRoleMenu entity : rightList){
        jpaRepository.save(entity);
      }
    }


    return true;
  }

  @Override
  public List<SysRoleMenu> findInRoleIdList(List<Long> roleIdList) {
    return CollectionUtils.isEmpty(roleIdList)?null:jpaRepository.findInRoleId(roleIdList);
  }

  @Override
  public List<SysRoleMenu> findByMenuId(Long menuId) {
    return null == menuId?null:jpaRepository.findByMenuId(menuId);
  }

  @Override
  public void removeList(Long menuId) {
    List<SysRoleMenu> list = findByMenuId(menuId);
    if(CollectionUtils.isNotEmpty(list)){
      jpaRepository.deleteAll(list);
    }
  }



}
