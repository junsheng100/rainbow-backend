package com.rainbow.user.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.entity.UserRole;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface UserRoleDao extends BaseDao<UserRole,Long> {

  String getRoleName(String userId);


  Map<String, List<UserRole>> findUseRoleList(List<String> userIdList);

  void storeUserInfo(@Valid UserInfo entity);

  List<UserRole> findByUserId(String userId);

  List<UserRole> findInRoleId(List<Long> roleIdList);

  void removeList(List<Long> roleIdList);
}
