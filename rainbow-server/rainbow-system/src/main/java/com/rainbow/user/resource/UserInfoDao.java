package com.rainbow.user.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.user.entity.UserInfo;

import java.time.LocalDateTime;
import java.util.List;

public interface UserInfoDao extends BaseDao<UserInfo, String> {
  UserInfo findById(String userId);

  boolean isUserType(String userType,String userId);


  UserInfo findByUserName(String userName);

  boolean existsByUserName(String userName);

  List<UserInfo> findInDeptId(List<Long> deptIdList);

  List<UserInfo> findUserAll();

  Long countUser();


  void updateLogin(String userId, LocalDateTime login);

  void updateLogout(String userId, LocalDateTime logout);

  List<UserInfo> findInUserId(List<String> list);
}
