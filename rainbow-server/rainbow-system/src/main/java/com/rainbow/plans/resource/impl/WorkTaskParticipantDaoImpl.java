package com.rainbow.plans.resource.impl;

import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.model.domain.LoginUser;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.plans.entity.WorkPlanTask;
import com.rainbow.plans.entity.WorkTaskParticipant;
import com.rainbow.plans.repository.WorkTaskParticipantRepository;
import com.rainbow.plans.resource.WorkTaskParticipantDao;
import com.rainbow.user.entity.DeptInfo;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.repository.DeptInfoRepository;
import com.rainbow.user.repository.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.plans.resource.impl
 * @Filename：WorkTaskParticipantDaoImpl
 * @Date：2025/9/21 17:56
 * @Describe:
 */
@Slf4j
@Component
public class WorkTaskParticipantDaoImpl extends BaseDaoImpl<WorkTaskParticipant, String, WorkTaskParticipantRepository> implements WorkTaskParticipantDao {

  @Autowired
  private UserInfoRepository userInfoRepository;
  @Autowired
  private DeptInfoRepository deptInfoRepository;


  @Override
  public boolean stortList(WorkPlanTask task, List<WorkTaskParticipant> participants) {

    if (null == task || CollectionUtils.isEmpty(participants))
      return false;
    String taskId = task.getId();
    String planId = task.getPlanId();

    if (StringUtils.isNotBlank(taskId)) {
      deleteByTaskId(taskId);
      String status = UseStatus.NO.getCode();
      LoginUser user = getLoginUser();
      LocalDateTime date = LocalDateTime.now();
      if (CollectionUtils.isNotEmpty(participants)) {
        for (WorkTaskParticipant entity : participants) {

          entity.setPlanId(planId);
          entity.setTaskId(taskId);
          entity.setLcu(user.getUserName());
          entity.setFcu(user.getUserName());
          entity.setLcd(date);
          entity.setFcd(date);
          entity.setStatus(status);
          /// //////////////
          super.store(entity);
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public void convertCollection(List<WorkTaskParticipant> list) {
    if (CollectionUtils.isEmpty(list))
      return;
    List<String> userIdList = list.stream().map(WorkTaskParticipant::getUserId).collect(Collectors.toList());

    if (CollectionUtils.isEmpty(userIdList))
      return;
    List<UserInfo> userInfoList = userInfoRepository.findInUserId(userIdList);

    List<Long> deptIdList = userInfoList.stream().map(UserInfo::getDeptId).collect(Collectors.toList());
    List<DeptInfo> deptInfoList = deptInfoRepository.findInId(deptIdList);

    for (WorkTaskParticipant entity : list) {
      String userId = entity.getUserId();
      if (StringUtils.isNotBlank(userId)) {
        UserInfo user = userInfoList.stream().filter(u -> u.getUserId().equals(userId)).findFirst().orElse(null);
        if (null != user) {
          entity.setUserName(user.getUserName());
          entity.setUserAvatar(user.getAvatar());
          Long deptId = user.getDeptId();
          if (null != deptId && CollectionUtils.isNotEmpty(deptInfoList)) {
            DeptInfo t = deptInfoList.stream().filter(d -> d.getDeptId().equals(deptId)).findFirst().orElse(null);
            if (null != t) {
              entity.setDeptName(t.getDeptName());
            }
          }
        }
      }

    }
  }

  @Override
  public List<WorkTaskParticipant> findInTaskId(List<String> taskIdList) {
    return CollectionUtils.isEmpty(taskIdList) ? null : jpaRepository.findInTaskId(taskIdList);
  }

  @Override
  public boolean removeByTaskId(String taskId) {
    if (StringUtils.isNotBlank(taskId)) {
      List<WorkTaskParticipant> list = findByTaskId(taskId);
      if (CollectionUtils.isNotEmpty(list))
        jpaRepository.deleteAll(list);
      return true;
    }
    return false;
  }

  public void deleteByTaskId(String taskId) {
    if (StringUtils.isBlank(taskId))
      return;
    List<WorkTaskParticipant> list = findByTaskId(taskId);
    if (CollectionUtils.isNotEmpty(list)) {
      jpaRepository.deleteAll(list);
    }
  }

  public List<WorkTaskParticipant> findByTaskId(String taskId) {
    if (StringUtils.isBlank(taskId))
      return null;
    return jpaRepository.findByTaskId(taskId);
  }
}
