package com.rainbow.user.resource.impl;

import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.user.entity.SysRole;
import com.rainbow.user.repository.SysRoleRepository;
import com.rainbow.user.resource.SysRoleDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SysRoleDaoImpl extends BaseDaoImpl<SysRole, Long,SysRoleRepository> implements SysRoleDao {
  @Override
  public List<SysRole> findInId(List<Long> roleIdList) {
    return CollectionUtils.isEmpty(roleIdList)?null:jpaRepository.findInId(roleIdList);
  }
}
