package com.rainbow.plans.resource.impl;

import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.RandomId;
import com.rainbow.plans.entity.WorkPlanParticipant;
import com.rainbow.plans.enums.ParticipantRole;
import com.rainbow.plans.resource.WorkPlanParticipantDao;
import com.rainbow.plans.repository.WorkPlanParticipantRepository;
import com.rainbow.user.entity.DeptInfo;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.repository.DeptInfoRepository;
import com.rainbow.user.repository.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.awt.image.Kernel;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 计划参与者数据访问实现
 *
 * @author rainbow
 * @since 2024-01-01
 */
@Slf4j
@Component
public class WorkPlanParticipantDaoImpl extends BaseDaoImpl<WorkPlanParticipant, String, WorkPlanParticipantRepository> implements WorkPlanParticipantDao {

  @Autowired
  private UserInfoRepository userInfoRepository;
  @Autowired
  private DeptInfoRepository deptInfoRepository;

  @Override
  public List<WorkPlanParticipant> findByPlanId(String planId) {
    return jpaRepository.findByPlanId(planId);
  }

  @Override
  public List<WorkPlanParticipant> findByUserId(String userId) {
    return jpaRepository.findByUserId(userId);
  }

  @Override
  public List<WorkPlanParticipant> findByRole(ParticipantRole role) {
    return jpaRepository.findByRole(role);
  }

  @Override
  public WorkPlanParticipant findByPlanIdAndUserId(String planId, String userId) {
    return jpaRepository.findByPlanIdAndUserId(planId, userId);
  }

  @Override
  public List<WorkPlanParticipant> findByPlanIdAndRole(String planId, ParticipantRole role) {
    return jpaRepository.findByPlanIdAndRole(planId, role);
  }

  @Override
  public boolean existsByPlanIdAndUserId(String planId, String userId) {
    return jpaRepository.existsByPlanIdAndUserId(planId, userId);
  }

  @Override
  public long countByPlanId(String planId) {
    return jpaRepository.countByPlanId(planId);
  }

  @Override
  public void deleteByPlanId(String planId) {
    // 使用Repository的批量删除方法
    jpaRepository.deleteByPlanId(planId);
  }

  @Override
  public WorkPlanParticipant findCreateByPlanId(String planId) {
    if (StringUtils.isBlank(planId))
      return null;
    return jpaRepository.findCreateByPlanId(planId, ParticipantRole.CREATOR);
  }


  @Override
  public void updateByPlanIdAndRole(String planId, List<WorkPlanParticipant> participants) {
    List<WorkPlanParticipant> list = jpaRepository.findByPlanId(planId);

    if (CollectionUtils.isNotEmpty(list)) {
      jpaRepository.deleteAll(list);
    }

    if (CollectionUtils.isNotEmpty(participants)) {
      String userName = getLoginUser().getUserName();
      LocalDateTime now = LocalDateTime.now();
      participants.forEach(t -> {
        t.setId(RandomId.generateShortUuid());
        t.setFcu(userName);
        t.setFcd(now);
        t.setPlanId(planId);
        t.setLcu(userName);
        t.setLcd(now);
        t.setStatus(UseStatus.NO.getCode());
      });

      /// ////////////////////////////
      jpaRepository.saveAll(participants);
    }
  }

  @Override
  public List<String> findUserIdByPlanId(String planId) {
    return StringUtils.isBlank(planId) ? null : jpaRepository.findUserIdByPlanId(planId);
  }

  @Override
  public void convertCollection(List<WorkPlanParticipant> list) {
    if (CollectionUtils.isEmpty(list))
      return;
    List<String> userIdList = list.stream().filter(t -> StringUtils.isNotBlank(t.getUserId())).map(WorkPlanParticipant::getUserId).collect(Collectors.toList());
    if (CollectionUtils.isNotEmpty(userIdList)) {
      List<UserInfo> userInfoList = userInfoRepository.findInUserId(userIdList);

      List<Long> deptIdList = userInfoList.stream().filter(t -> null != t.getDeptId()).map(UserInfo::getDeptId).collect(Collectors.toList());

      List<DeptInfo> deptList = deptInfoRepository.findInId(deptIdList);

      Map<String, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getUserId, u -> u));
      list.forEach(t -> {
        UserInfo userInfo = (UserInfo) MapUtils.getObject(userInfoMap, t.getUserId(), null);
        if (null != userInfo) {
          t.setUserName(userInfo.getNickname());
          t.setUserAvatar(userInfo.getAvatar());

          Long deptId = userInfo.getDeptId();
          if (null != deptId && CollectionUtils.isNotEmpty(deptList)) {
            String deptName = deptList.stream().filter(d -> deptId.equals(d.getDeptId())).map(DeptInfo::getDeptName).collect(Collectors.joining(ChartEnum.COMMA.getCode()));
            userInfo.setDeptName(deptName);
            t.setDeptName(deptName);
          }
        }
      });
    }

  }

  @Override
  public List<WorkPlanParticipant> findInPlanId(List<String> plandIdList) {
    if (CollectionUtils.isEmpty(plandIdList))
      return null;
    return jpaRepository.findInPlanId(plandIdList);
  }

  @Override
  public void deleteByPlanIdAndRole(String planId, ParticipantRole participantRole) {
      jpaRepository.deleteByPlanIdAndRole(planId, participantRole);
  }


}
