package com.rainbow.user.resource.impl;

import com.rainbow.base.constant.DataConstant;
import com.rainbow.base.exception.DataException;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.user.entity.SysMenu;
import com.rainbow.user.enums.MenuTypeEnum;
import com.rainbow.user.repository.SysMenuRepository;
import com.rainbow.user.resource.SysMenuDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class SysMenuDaoImpl extends BaseDaoImpl<SysMenu, Long, SysMenuRepository> implements SysMenuDao {


  @Override
  public SysMenu check(SysMenu entity) {
    SysMenu old = getOne(entity);

    String menuType = entity.getMenuType();

    Assert.notNull(menuType, "菜单类型不能为空值");
    MenuTypeEnum typeEnum = MenuTypeEnum.getByCode(menuType);
    Assert.notNull(typeEnum, "菜单类型错误");
    Long parentId = entity.getParentId();

    if (MenuTypeEnum.CATALOG.getCode().equals(menuType)) {
      parentId = null == parentId ? DataConstant.MENU_ROOT : parentId;
    }
    if (MenuTypeEnum.LEAF.getCode().equals(menuType)) {
      Assert.notNull(parentId, "上级目录ID 不能为空");
    }
    if (MenuTypeEnum.FUNCTION.getCode().equals(menuType)) {
      Assert.notNull(parentId, "上级菜单ID 不能为空");
    }
    String perms = entity.getPerms();
    perms = StringUtils.isBlank(perms)?"":perms.trim();
    entity.setPerms(perms);

    entity.setParentId(parentId);

    return old;
  }


  @Override
  public List<SysMenu> findInMenuId(List<Long> menuIdList) {
    return CollectionUtils.isEmpty(menuIdList) ? null : jpaRepository.findAllById(menuIdList);
  }

  @Override
  public List<SysMenu> findInMenuType(List<String> typeList) {
    if (CollectionUtils.isEmpty(typeList))
      return Collections.emptyList();
    if (typeList.size() == 1)
      return findByMenuType(typeList.get(0));
    return jpaRepository.findInMenuType(typeList);
  }

  @Override
  public List<SysMenu> findByMenuType(String menuType) {
    return StringUtils.isBlank(menuType) ? null : jpaRepository.findByMenuType(menuType);
  }

  @Override
  public List<SysMenu> findAll() {
    return jpaRepository.findAll();
  }

  @Override
  public List<SysMenu> findChildren(Long parentId) {
    if (null == parentId)
      return null;
    SysMenu data = get(parentId);
    if (null == data)
      return null;
    String menuType = data.getMenuType();

    return jpaRepository.findChildren(parentId, menuType);
  }

  @Override
  public Long countChildren(Long menuId) {
    return null == menuId ? null : jpaRepository.countChildren(menuId);
  }

  @Override
  public List<SysMenu> findMenuAll() {
    return jpaRepository.findMenuAll();
  }

  @Override
  public List<SysMenu> findMenuInMenuId(List<Long> menuIdList) {
    return jpaRepository.findMenuInMenuId(menuIdList);
  }

  @Override
  public List<String> findInterfaceId() {
    return jpaRepository.findInterfaceId();
  }

  @Override
  public List<SysMenu> findInterfaceId(List<String> interfaceIdList) {
    return CollectionUtils.isEmpty(interfaceIdList)?null:super.jpaRepository.findInterfaceId(interfaceIdList);
  }

  @Override
  public List<SysMenu> findNotInInterfaceId(List<String> interfaceIdList) {
    return CollectionUtils.isEmpty(interfaceIdList)?null:super.jpaRepository.findNotInInterfaceId(interfaceIdList);
  }

  @Override
  public List<String> findPermsByUserId(String userId) {
    if( StringUtils.isBlank(userId))
      throw new DataException("用户数据不能为空");

    return jpaRepository.findPermsByUserId(userId);
  }


}
