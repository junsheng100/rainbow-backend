package com.rainbow.files.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.files.entity.SysResource;

public interface SysResourceDao extends BaseDao<SysResource,String> {

  SysResource findByFileUrl(String fileUrl);

  SysResource findByMd5Code(String md5);


}
