package com.rainbow.user.resource.impl;

import cn.hutool.core.lang.Assert;
import com.rainbow.base.exception.DataException;
import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.repository.UserInfoRepository;
import com.rainbow.user.resource.UserInfoDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class UserInfoDaoImpl extends BaseDaoImpl<UserInfo, String, UserInfoRepository> implements UserInfoDao {

  @Override
  public Map<String, Predicate> commonParams(Root<UserInfo> root, CriteriaBuilder cb, BaseVo<UserInfo> vo) {
    Map<String, Predicate> mp = super.dataManager.getPredicates(root, cb, vo);
    UserInfo data = vo.getData();
    if (null == data)
      throw new NullPointerException("Data is null");
    String keyword = data.getKeyword();
    if (StringUtils.isNotBlank(keyword)) {

      Predicate p1 = cb.like(root.get("userName"), "%" + keyword + "%");
      Predicate p2 = cb.like(root.get("nickname"), "%" + keyword + "%");
      mp.put("keyword", cb.or(p1, p2));

    }

    return mp;
  }


  @Override
  public UserInfo findById(String userId) {
    return super.jpaRepository.findById(userId).orElseThrow(() -> new DataException("用户不存在"));
  }

  @Override
  public boolean isUserType(String userType,String userId) {
    int count = super.jpaRepository.isUserType(userType,userId);
    if (count > 0) {
      return true;
    }
    return false;
  }

  @Override
  public UserInfo findByUserName(String userName) {
    return super.jpaRepository.findByUserName(userName).orElseThrow(() -> new DataException("用户不存在"));
  }

  @Override
  public boolean existsByUserName(String userName) {
    return super.jpaRepository.existsByUserName(userName);
  }

  @Override
  public List<UserInfo> findInDeptId(List<Long> deptIdList) {
    return CollectionUtils.isEmpty(deptIdList) ? null : super.jpaRepository.findInDeptId(deptIdList);
  }

  @Override
  public List<UserInfo> findUserAll() {
    return super.jpaRepository.findUserAll();
  }

  @Override
  public Long countUser() {
    return jpaRepository.countUser();
  }

  @Override
  public void updateLogin(String userId, LocalDateTime login) {
    Assert.notBlank(userId, "userId is null");
    login = login == null ? LocalDateTime.now() : login;
    jpaRepository.updateLogin(userId, login);
  }

  @Override
  public void updateLogout(String userId, LocalDateTime logout) {
    Assert.notBlank(userId, "userId is null");
    logout = logout == null ? LocalDateTime.now() : logout;
    jpaRepository.updateLogout(userId, logout);
  }

  @Override
  public List<UserInfo> findInUserId(List<String> idList) {
    return CollectionUtils.isEmpty(idList) ? Collections.emptyList() : jpaRepository.findInUserId(idList);
  }


}
