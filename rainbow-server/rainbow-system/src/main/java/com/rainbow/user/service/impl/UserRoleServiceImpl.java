package com.rainbow.user.service.impl;

import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.user.entity.UserRole;
import com.rainbow.user.resource.UserRoleDao;
import com.rainbow.user.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole,Long, UserRoleDao> implements UserRoleService {
}
