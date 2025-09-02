package com.rainbow.user.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.user.entity.UserPasswd;

public interface UserPasswdService extends BaseService<UserPasswd,Long> {

  UserPasswd getByUserId(String userId);
}
