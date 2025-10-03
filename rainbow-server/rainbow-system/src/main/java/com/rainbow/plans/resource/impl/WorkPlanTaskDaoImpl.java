package com.rainbow.plans.resource.impl;

import com.rainbow.base.exception.DataException;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.plans.entity.WorkPlanTask;
import com.rainbow.plans.enums.TaskStatus;
import com.rainbow.plans.resource.WorkPlanTaskDao;
import com.rainbow.plans.repository.WorkPlanTaskRepository;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.repository.UserInfoRepository;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 计划任务数据访问实现
 *
 * @author rainbow
 * @since 2024-01-01
 */
@Slf4j
@Component
public class WorkPlanTaskDaoImpl extends BaseDaoImpl<WorkPlanTask, String, WorkPlanTaskRepository> implements WorkPlanTaskDao {

  @Autowired
  private WorkPlanTaskRepository workPlanTaskRepository;
  @Autowired
  private UserInfoRepository userInfoRepository;

  public WorkPlanTask check(WorkPlanTask entity) {
    WorkPlanTask old = getOne(entity);
    initData(entity);
    return old;
  }


  @Override
  public List<WorkPlanTask> findByPlanId(String planId) {
    return workPlanTaskRepository.findByPlanId(planId);
  }

  @Override
  public List<WorkPlanTask> findByAssigneeId(String assigneeId) {
    return workPlanTaskRepository.findByAssigneeId(assigneeId);
  }

  @Override
  public List<WorkPlanTask> findByStatus(TaskStatus status) {
    return workPlanTaskRepository.findByTaskStatus(status);
  }

  @Override
  public List<WorkPlanTask> findByParentTaskId(String parentTaskId) {
    return workPlanTaskRepository.findByParentTaskId(parentTaskId);
  }

  @Override
  public List<WorkPlanTask> findByPlanIdAndStatus(String planId, TaskStatus status) {
    return workPlanTaskRepository.findByPlanIdAndStatus(planId, status);
  }

  @Override
  public List<WorkPlanTask> findMilestoneTasks(String planId) {
    return workPlanTaskRepository.findMilestoneTasks(planId);
  }

  @Override
  public long countByPlanId(String planId) {
    return workPlanTaskRepository.countByPlanId(planId);
  }

  @Override
  public long countCompletedByPlanId(String planId) {
    return workPlanTaskRepository.countCompletedByPlanId(planId);
  }

  @Override
  public double getTaskCompletionRate(String planId) {
    Double result = workPlanTaskRepository.getTaskCompletionRate(planId);
    return result != null ? result : 0.0;
  }

  @Override
  public List<WorkPlanTask> findCriticalPathTasks(String planId) {
    if (StringUtils.isBlank(planId))
      return Collections.emptyList();
    return jpaRepository.findCriticalPathTasks(planId);
  }

  @Override
  public void convertData(WorkPlanTask entity) {
    if (null == entity)
      return;
    String assigneeId = entity.getAssigneeId();
    if (StringUtils.isNotBlank(assigneeId)) {
      UserInfo userInfo = userInfoRepository.getByUserId(assigneeId);
      entity.setAssigneeName(null == userInfo ? "" : userInfo.getNickname());
    }
  }

  @Override
  public void convertCollection(List<WorkPlanTask> list) {
    if (CollectionUtils.isEmpty(list))
      return;
    List<String> assigneeIds = list.stream().filter(t -> StringUtils.isNotBlank(t.getAssigneeId()))
            .map(WorkPlanTask::getAssigneeId).collect(Collectors.toList());
    List<UserInfo> userInfoList = userInfoRepository.findInUserId(assigneeIds);

    for (WorkPlanTask t : list) {
      if (CollectionUtils.isNotEmpty(userInfoList)) {
        UserInfo userInfo = userInfoList.stream().filter(u -> u.getUserId().equals(t.getAssigneeId())).findFirst().orElse(null);
        t.setAssigneeName(null == userInfo ? "" : userInfo.getNickname());
      }
    }
  }

  @Override
  public boolean existsById(String id) {
    if (StringUtils.isNotBlank(id))
      return jpaRepository.existsById(id);
     throw new DataException(" ID IS NULL ");
  }

  private void initData(WorkPlanTask entity) {

    if (null == entity)
      return;

    BigDecimal progress = BigDecimal.ZERO;
    BigDecimal estimatedHours = BigDecimal.ZERO;
    BigDecimal actualHours = BigDecimal.ZERO;
    Integer sortOrder = 1;
    boolean isMilestone = false;
    Integer floatDays = 0;
    boolean isCriticalPath = false;
    Integer estimatedDuration = 1;
    Integer actualDuration = 0;

    if (null == entity.getProgress())
      entity.setProgress(progress);

    if (null == entity.getEstimatedHours())
      entity.setEstimatedHours(estimatedHours);

    if (null == entity.getActualHours())
      entity.setActualHours(actualHours);

    if (null == entity.getSortOrder())
      entity.setSortOrder(sortOrder);

    if (null == entity.getIsMilestone())
      entity.setIsMilestone(isMilestone);

    if (null == entity.getFloatDays())
      entity.setFloatDays(floatDays);

    if (null == entity.getIsCriticalPath())
      entity.setIsCriticalPath(isCriticalPath);

    if (null == entity.getEstimatedDuration())
      entity.setEstimatedDuration(estimatedDuration);

    if (null == entity.getActualDuration())
      entity.setActualDuration(actualDuration);

  }
}
