package com.rainbow.appdoc.resource.impl;

import com.rainbow.appdoc.entity.AppReference;
import com.rainbow.appdoc.repository.ApiReferenceRepository;
import com.rainbow.appdoc.resource.ApiReferenceDao;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.appInfo.resource.impl
 * @Filename：ApiReferenceDaoImpl
 * @Date：2025/7/27 11:54
 */
@Slf4j
@Component
public class ApiReferenceDaoImpl extends BaseDaoImpl<AppReference, String, ApiReferenceRepository> implements ApiReferenceDao {
}
