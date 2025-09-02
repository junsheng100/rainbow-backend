package com.rainbow.appdoc.service.impl;

import com.rainbow.appdoc.entity.AppModelType;
import com.rainbow.appdoc.resource.ApiModelTypeDao;
import com.rainbow.appdoc.service.ApiModelTypeService;
import com.rainbow.base.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.appInfo.service.impl
 * @Filename：ApiModelTypeServiceImpl
 * @Date：2025/7/27 11:58
 * @Describe:
 */
@Slf4j
@Service
public class ApiModelTypeServiceImpl extends BaseServiceImpl<AppModelType,String, ApiModelTypeDao> implements ApiModelTypeService {
}
