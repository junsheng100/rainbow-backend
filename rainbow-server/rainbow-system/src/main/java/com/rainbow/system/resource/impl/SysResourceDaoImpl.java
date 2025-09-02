package com.rainbow.system.resource.impl;

import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.system.entity.SysResource;
import com.rainbow.system.repository.SysResourceRepository;
import com.rainbow.system.resource.SysResourceDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SysResourceDaoImpl extends BaseDaoImpl<SysResource,Long, SysResourceRepository> implements SysResourceDao {
  @Override
  public SysResource findByFileUrl(String fileUrl) {
    return StringUtils.isBlank(fileUrl)?null:jpaRepository.findByFileUrl(fileUrl);
  }
}
