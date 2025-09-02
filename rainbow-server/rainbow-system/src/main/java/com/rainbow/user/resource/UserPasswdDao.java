package com.rainbow.user.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.user.entity.UserPasswd;

public interface UserPasswdDao extends BaseDao<UserPasswd,Long> {


  UserPasswd getByUserId(String userId);



  String getPassword(String password);
}
