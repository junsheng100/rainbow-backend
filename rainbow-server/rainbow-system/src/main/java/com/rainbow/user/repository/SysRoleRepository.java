package com.rainbow.user.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.user.entity.SysRole;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysRoleRepository extends BaseRepository<SysRole,Long> {

  @Query("select t from SysRole t where t.roleId in (?1)")
  List<SysRole> findInId(List<Long> roleIdList);

}
