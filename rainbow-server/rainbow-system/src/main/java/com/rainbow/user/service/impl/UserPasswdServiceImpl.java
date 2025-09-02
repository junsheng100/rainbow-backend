package com.rainbow.user.service.impl;

import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.user.entity.UserPasswd;
import com.rainbow.user.resource.UserPasswdDao;
import com.rainbow.user.service.UserPasswdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserPasswdServiceImpl extends BaseServiceImpl<UserPasswd,Long, UserPasswdDao> implements UserPasswdService {


  @Override
  public UserPasswd getByUserId(String userId) {
    return baseDao.getByUserId(userId);
  }

}
