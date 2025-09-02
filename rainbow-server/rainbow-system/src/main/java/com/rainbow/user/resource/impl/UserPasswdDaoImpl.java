package com.rainbow.user.resource.impl;

import com.rainbow.base.exception.BizException;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.user.entity.UserPasswd;
import com.rainbow.user.repository.UserPasswdRepository;
import com.rainbow.user.resource.UserPasswdDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserPasswdDaoImpl extends BaseDaoImpl<UserPasswd,Long, UserPasswdRepository> implements UserPasswdDao {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserPasswd getByUserId(String userId) {
    return StringUtils.isNotEmpty(userId) ? jpaRepository.getByUserId(userId) : null;
  }

  @Override
  public String getPassword(String password) {
    if (org.apache.commons.lang3.StringUtils.isBlank(password))
      throw new BizException("密码不能为空");
    return passwordEncoder.encode(password);
  }
}
