package com.rainbow.user.service.impl;

import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.user.entity.SysRoleMenu;
import com.rainbow.user.resource.SysRoleMenuDao;
import com.rainbow.user.service.SysRoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SysRoleMenuServiceImpl extends BaseServiceImpl<SysRoleMenu,Long, SysRoleMenuDao> implements SysRoleMenuService {
}
