package com.rainbow.user.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.user.entity.UserPasswd;

public interface UserPasswdRepository extends BaseRepository<UserPasswd, Long> {

  UserPasswd getByUserId(String userId);

}
