package com.rainbow.user.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.user.entity.UserInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface UserInfoRepository extends BaseRepository<UserInfo, String> {


  boolean existsByUserName(String userName);

  Optional<UserInfo> findByUserName(String userName);

  @Query("select u from UserInfo u where u.status = '0' and u.deptId in (?1) ")
  List<UserInfo> findInDeptId(List<Long> deptIdList);

  @Query("select u.userId from UserInfo u where u.status = '0' and u.deptId in (?1) ")
  List<String> findUserIdInDeptId(List<Long> deptIdList);

  @Query("select u from UserInfo u where u.status = '0' and u.userId in (?1) ")
  List<UserInfo> findInUserId(List<String> userIdLit);

  @Query("select u from UserInfo u where u.status = '0' and u.disabled = '0' ")
  List<UserInfo> findUserAll();

  @Query("select count(u.userId) from UserInfo u where u.status = '0' and u.disabled = '0' ")
  Long countUser();

  @Modifying
  @Query(value = "update sys_user_info  set  login = ?2 where user_id = ?1", nativeQuery = true)
  void updateLogin(String userId, LocalDateTime login);

  @Modifying
  @Query(value = "update sys_user_info  set  logout = ?2 where user_id = ?1", nativeQuery = true)
  void updateLogout(String userId, LocalDateTime logout);

  @Query("select count(t) from UserInfo  t where t.userType = ?1 and t.userId = ?2 ")
  int isUserType(String name, String userId);

}