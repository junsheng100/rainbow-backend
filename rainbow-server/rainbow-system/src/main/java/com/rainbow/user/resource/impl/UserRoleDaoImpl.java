package com.rainbow.user.resource.impl;

import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.user.entity.SysRole;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.entity.UserRole;
import com.rainbow.user.repository.SysRoleRepository;
import com.rainbow.user.repository.UserRoleRepository;
import com.rainbow.user.resource.UserRoleDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserRoleDaoImpl extends BaseDaoImpl<UserRole,Long, UserRoleRepository> implements UserRoleDao {

  @Autowired
  private SysRoleRepository roleRepository;

  @Override
  public String getRoleName(String userId) {
    if (StringUtils.isNotBlank(userId)) {
      List<Long> roleIdList = jpaRepository.findRoleIdByUserId(userId);
      List<SysRole> roleList = CollectionUtils.isEmpty(roleIdList) ? null : roleRepository.findAllById(roleIdList);
      String roleName = CollectionUtils.isEmpty(roleList) ? "" : roleList.stream().map(SysRole::getRoleName).distinct().collect(Collectors.joining(ChartEnum.COMMA.getCode()));
      return roleName;
    }
    return "";
  }

  @Override
  public Map<String, List<UserRole>> findUseRoleList(List<String> userIdList) {
    Map<String, List<UserRole>> map = new HashMap<>();
    List<UserRole> list = jpaRepository.findInUserId(userIdList);
    if (CollectionUtils.isNotEmpty(list)) {
      List<Long> roleIdList = list.stream().map(UserRole::getRoleId).distinct().collect(Collectors.toList());
      List<SysRole> roleList = roleRepository.findAllById(roleIdList);

      list.stream().forEach(ur->{
        String roleName = roleList.stream().filter(t->ur.getRoleId().equals(t.getRoleId())).map(SysRole::getRoleName).distinct().collect(Collectors.joining(ChartEnum.COMMA.getCode()));
        ur.setRoleName(roleName);
      });
      map = list.stream().collect(Collectors.groupingBy(UserRole::getUserId));
    }
    return map;
  }

  @Override
  public void storeUserInfo(UserInfo userInfo) {
    if (null == userInfo)
      return;
    if (StringUtils.isBlank(userInfo.getUserId()))
      return;
    if (CollectionUtils.isEmpty(userInfo.getRoleIdList()))
      return;
    String userId = userInfo.getUserId();
    List<Long> roleIdList = userInfo.getRoleIdList();
    List<UserRole> oldList = jpaRepository.findByUserId(userId);

    if (CollectionUtils.isNotEmpty(oldList))
      jpaRepository.deleteAll(oldList);

    String userName = super.getUserName();
    LocalDateTime date = LocalDateTime.now();

    for(Long roleId:roleIdList){
      UserRole userRole = new UserRole(userId,roleId,userName,date,userId,date, UseStatus.NO.getCode());
      jpaRepository.save(userRole);
    }
  }

  @Override
  public List<UserRole> findByUserId(String userId) {
    return StringUtils.isBlank(userId)?null:jpaRepository.findByUserId(userId);
  }

  @Override
  public List<UserRole> findInRoleId(List<Long> roleIdList) {
    return CollectionUtils.isEmpty(roleIdList)?null:jpaRepository.findInRoleId(roleIdList);
  }

  @Override
  public void removeList(List<Long> roleIdList) {
    List<UserRole> list =  findInRoleId(roleIdList);
    if(CollectionUtils.isNotEmpty(list)){
      jpaRepository.deleteAll(list);
    }
  }


}
