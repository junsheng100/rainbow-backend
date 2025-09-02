package com.rainbow.system.service;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.service.BaseService;
import com.rainbow.system.entity.SysLogin;
import com.rainbow.system.model.vo.LoginData;
import com.rainbow.user.entity.UserInfo;

import java.util.List;

public interface SysLoginService extends BaseService<SysLogin,Long> {
  SysLogin saveLogin(UserInfo user);

  SysLogin saveLogout(UserInfo userInfo);

  Boolean cleanAll();


  Boolean saveUserLogin(String userId);

  Boolean saveUserLogout(String userId);

  PageData<SysLogin> findOnLine(CommonVo<String> vo);

  Boolean logout(String... userId);

  Boolean logoutAll();

  List<LoginData> totalAreaPro();
}
