package com.rainbow.user.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.user.entity.SysRoleMenu;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysRoleMenuRepository extends BaseRepository<SysRoleMenu,Long> {

  @Query("select t from SysRoleMenu t where t.roleId = ?1  ")
  List<SysRoleMenu> findByRoleId(Long roleId);

  @Query("select t from SysRoleMenu t where t.roleId in (?1)  ")
  List<SysRoleMenu> findInRoleId(List<Long> list);

  @Query("select t from SysRoleMenu t where t.menuId = ?1  ")
  List<SysRoleMenu> findByMenuId(Long menuId);


}
