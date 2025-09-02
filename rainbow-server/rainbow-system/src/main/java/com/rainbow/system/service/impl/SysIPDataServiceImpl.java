package com.rainbow.system.service.impl;

import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.system.entity.SysIPData;
import com.rainbow.system.resource.SysIPDataDao;
import com.rainbow.system.service.SysIPDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.system.service.impl
 * @Filename：SysIPAddressServiceImpl
 * @Describe:
 */
@Slf4j
@Service
public class SysIPDataServiceImpl extends BaseServiceImpl<SysIPData,String, SysIPDataDao> implements SysIPDataService {
}
