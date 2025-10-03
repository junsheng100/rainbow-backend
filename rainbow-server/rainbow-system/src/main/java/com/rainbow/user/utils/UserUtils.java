package com.rainbow.user.utils;

import com.rainbow.base.enums.UserType;
import com.rainbow.user.entity.UserInfo;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.user.utils
 * @Filename：UserUtils
 * @Date：2025/9/16 19:53
 * @Describe:
 */
public class UserUtils {

  public static boolean isAdmin(UserInfo user){
    if(null != user){
      return UserType.ADMIN.name().equals(user.getUserType());
    }
    return false;
  }
}
