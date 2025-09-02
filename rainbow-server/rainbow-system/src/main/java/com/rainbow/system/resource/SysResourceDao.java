package com.rainbow.system.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.system.entity.SysResource;

public interface SysResourceDao extends BaseDao<SysResource,Long> {

  SysResource findByFileUrl(String fileUrl);
}
