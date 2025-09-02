package com.rainbow.user.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.user.entity.SysMenu;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysMenuRepository extends BaseRepository<SysMenu, Long> {

  @Query("select t from SysMenu t where t.menuType = ?1 order by orderNum asc ")
  List<SysMenu> findByMenuType(String menuType);

  @Query("select t from SysMenu t where t.menuType in (?1) order by orderNum asc ")
  List<SysMenu> findInMenuType(List<String> typeList);

  @Query("select t from SysMenu t where t.parentId = ?1 and t.menuType !=?2 order by t.orderNum  ")
  List<SysMenu> findChildren(Long parentId, String menuType);

  @Query("select count(t.id) from SysMenu t where t.parentId = ?1 ")
  Long countChildren(Long menuId);

  @Query("select t from SysMenu t where t.menuType in ('C','M') and t.visible = '0' and t.disabled = '0' order by t.orderNum  ")
  List<SysMenu> findMenuAll();


  @Query("select t from SysMenu t where t.menuType in ('C','M') and t.visible = '0' and t.disabled = '0' and t.menuId in (?1) order by t.orderNum  ")
  List<SysMenu> findMenuInMenuId(List<Long> menuIdList);

  @Query("select t.interfaceId from SysMenu t where  t.status = '0' and t.disabled = '0' order by t.orderNum  ")
  List<String> findInterfaceId();

  @Query("select t from SysMenu t where t.interfaceId in (?1) order by t.orderNum  ")
  List<SysMenu> findInterfaceId(List<String> interfaceIdList);

  @Query("select t from SysMenu t where t.interfaceId not in (?1)   ")
  List<SysMenu> findNotInInterfaceId(List<String> interfaceIdList);

  @Query("select distinct m.perms from SysMenu m ,SysRoleMenu srm, UserRole ur " +
          " where m.disabled = 0" +
          "   and m.id = srm.menuId " +
          "   and srm.roleId = ur.roleId" +
          "   and ur.userId = ?1 ")
  List<String> findPermsByUserId(String userId);
}
