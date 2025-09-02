package com.rainbow.user.service;

import com.rainbow.base.exception.AuthException;
import com.rainbow.base.service.BaseService;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.model.UserTotal;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserInfoService extends BaseService<UserInfo, String> {
  /**
   * 创建用户
   */
  UserInfo createUser(UserInfo userInfo);

  /**
   * 更新用户
   */
  UserInfo updateUser(String userId, UserInfo userInfo);

  /**
   * 根据用户名查找用户
   */
  UserInfo findByUserName(String userName);

  /**
   * 修改密码
   */
  void changePassword(String userId, String oldPassword, String newPassword);


  Boolean restPassword(String userId, String password);

  UserTotal total();

  String uploadFile(MultipartFile multipartFile, String localPath, String userId);


  List<String> getUserRoles(String userId) throws AuthException;

  List<String> getUserPermissions(String userId);
}