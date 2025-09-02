package com.rainbow.user.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.user.entity.UserRole;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleRepository extends BaseRepository<UserRole,Long> {

  @Query("select t.roleId from UserRole t where t.userId = ?1 ")
  List<Long> findRoleIdByUserId(String userId);


  @Query("select t.roleId from UserRole t where t.userId in (?1) ")
  List<Long> findRoleIdInUserId(List<String> userIdList);

  @Query("select t from UserRole t where t.userId in (?1) ")
  List<UserRole> findInUserId(List<String> userIdList);

  @Query("select t from UserRole t where t.userId = ?1 ")
  List<UserRole> findByUserId(String userId);

  @Query("select t from UserRole t where t.roleId in (?1) ")
  List<UserRole> findInRoleId(List<Long> roleIdList);


}
