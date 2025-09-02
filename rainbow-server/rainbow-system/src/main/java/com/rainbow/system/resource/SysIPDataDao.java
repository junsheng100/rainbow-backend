package com.rainbow.system.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.system.entity.SysIPData;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.system.resource
 * @Filename：SysIPAddressDao
 * @Date：2025/8/25 09:51
 * @Describe:
 */
public interface SysIPDataDao extends BaseDao<SysIPData,String> {

  SysIPData getByIpaddr(String ip);
}
