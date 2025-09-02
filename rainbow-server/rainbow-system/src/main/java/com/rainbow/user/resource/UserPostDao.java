package com.rainbow.user.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.entity.UserPost;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface UserPostDao extends BaseDao<UserPost,Long> {

  String getPostName(String userId);


  Map<String, List<UserPost>> findUserPostList(List<String> userIdList);

  void storeUserInfo(@Valid UserInfo entity);
}
