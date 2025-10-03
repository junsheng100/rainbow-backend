package com.rainbow.plans.service.impl;

import com.rainbow.base.exception.BizException;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.base.utils.CommonUtils;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.plans.entity.WorkPlan;
import com.rainbow.plans.entity.WorkPlanTask;
import com.rainbow.plans.entity.WorkTaskParticipant;
import com.rainbow.plans.enums.PlanStatus;
import com.rainbow.plans.enums.TaskStatus;
import com.rainbow.plans.model.request.WorkTaskRequest;
import com.rainbow.plans.resource.WorkPlanDao;
import com.rainbow.plans.resource.WorkPlanTaskDao;
import com.rainbow.plans.resource.WorkTaskParticipantDao;
import com.rainbow.plans.service.WorkPlanTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 计划任务服务实现
 *
 * @author rainbow
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkPlanTaskServiceImpl extends BaseServiceImpl<WorkPlanTask, String, WorkPlanTaskDao> implements WorkPlanTaskService {

  @Autowired
  private WorkTaskParticipantDao participantDao;
  @Autowired
  private WorkPlanDao planDao;

  @Override
  public List<WorkPlanTask> getTasksByPlanId(String planId) {
    return baseDao.findByPlanId(planId);
  }


  @Override
  public WorkPlanTask store(@Valid WorkPlanTask entity) {
    if (validate(entity)) {

      baseDao.store(entity);
      List<WorkTaskParticipant> participants = entity.getParticipants();
      participantDao.stortList(entity, participants);

      return entity;
    }
    throw new BizException("数据验证失败");
  }

  @Override
  public Boolean delete(String id) {
    baseDao.remove(id);
    participantDao.removeByTaskId(id);
    return true;
  }


  @Override
  @Transactional
  public void updateTaskProgress(String taskId, BigDecimal progress) {
    WorkPlanTask task = get(taskId);
    if (task == null) {
      throw new BizException("任务不存在");
    }

    if (progress.compareTo(BigDecimal.ZERO) < 0 || progress.compareTo(new BigDecimal("100")) > 0) {
      throw new BizException("进度值必须在0-100之间");
    }

    task.setProgress(progress);
    task.setLcd(LocalDateTime.now());

    // 如果进度达到100%，自动设置为已完成状态
    if (progress.compareTo(new BigDecimal("100")) == 0) {
      task.setTaskStatus(TaskStatus.COMPLETED);
      task.setActualEndDate(LocalDate.now());
    }

    baseDao.save(task);
    // log.info("更新任务进度成功，任务ID: {}, 进度: {}%", taskId, progress);
  }


  @Override
  @Transactional
  public void assignTask(String taskId, String assigneeId) {
    WorkPlanTask task = get(taskId);
    if (task == null) {
      throw new BizException("任务不存在");
    }

    task.setAssigneeId(assigneeId);
    task.setLcd(LocalDateTime.now());
    baseDao.save(task);

    // log.info("分配任务成功，任务ID: {}, 分配人: {}", taskId, assigneeId);
  }

  @Override
  @Transactional
  public void changeTaskStatus(String taskId, TaskStatus status) {
    WorkPlanTask task = get(taskId);
    if (task == null) {
      throw new BizException("任务不存在");
    }

    // 检查状态流转是否合法
    if (!task.getTaskStatus().canTransitionTo(status)) {
      throw new BizException("状态流转不合法，无法从" + task.getTaskStatus().getDescription() + "流转到" + status.getDescription());
    }

    TaskStatus oldStatus = task.getTaskStatus();
    task.setTaskStatus(status);
    task.setLcd(LocalDateTime.now());

    // 根据状态设置相应的时间
    switch (status) {
      case IN_PROGRESS:
        if (task.getActualStartDate() == null) {
          task.setActualStartDate(LocalDate.now());
        }
        break;
      case COMPLETED:
        task.setActualEndDate(LocalDate.now());
        task.setProgress(new BigDecimal("100"));
        break;
    }

    baseDao.save(task);
    // log.info("任务状态变更成功，任务ID: {}, 从{}变更为{}", taskId, oldStatus.getDescription(), status.getDescription());
  }

  @Override
  public List<WorkPlanTask> getMilestoneTasks(String planId) {
    return baseDao.findMilestoneTasks(planId);
  }

  @Override
  public double getTaskCompletionRate(String planId) {
    return baseDao.getTaskCompletionRate(planId);
  }

  @Override
  public List<WorkPlanTask> getUserTasks(String userId) {
    return baseDao.findByAssigneeId(userId);
  }

  @Override
  @Transactional
  public void updateTaskOrder(List<String> taskIds) {
    for (int i = 0; i < taskIds.size(); i++) {
      WorkPlanTask task = get(taskIds.get(i));
      if (task != null) {
        task.setSortOrder(i + 1);
        baseDao.save(task);
      }
    }
    // log.info("更新任务排序成功，任务数量: {}", taskIds.size());
  }

  @Override
  public List<WorkPlanTask> findTaskByPlanId(String planId) {
    return baseDao.findByPlanId(planId);
  }

  @Override
  public List<WorkPlanTask> calculateCriticalPath(String planId) {
    return Collections.emptyList();
  }


  public void convertData(WorkPlanTask entity) {

    if (null != entity) {
      baseDao.convertData(entity);
      List<WorkTaskParticipant> participantList = participantDao.findByTaskId(entity.getId());
      participantDao.convertCollection(participantList);

      entity.setParticipants(participantList);

    }

  }

  public void convertCollection(List<WorkPlanTask> list) {
    if (CollectionUtils.isNotEmpty(list)) {
      baseDao.convertCollection(list);
     /*
     List<String> taskIdList = list.stream().map(WorkPlanTask::getId).collect(Collectors.toList());
      List<WorkTaskParticipant> participantList = participantDao.findInTaskId(taskIdList);
      participantDao.convertCollection(participantList);
      for (WorkPlanTask task : list) {
        if (CollectionUtils.isNotEmpty(participantList)) {
          List<WorkTaskParticipant> participans = participantList.stream().filter(p -> p.getTaskId().equals(task.getId())).collect(Collectors.toList());
          task.setParticipants(participans);
        }
      }
      */
    }
  }


  public boolean validate(WorkPlanTask entity) {

    if (null == entity)
      throw new BizException("entity is null ");
    String planId = entity.getPlanId();

    if (StringUtils.isBlank(planId))
      throw new BizException("计划ID不能为空");

    WorkPlan plan = planDao.get(planId);
    if (null == plan)
      throw new BizException("计划不存在");
    if (null == plan.getPlanStatus()) {
      throw new BizException("任务状态不能为空");
    }

    return true;
  }


  /**
   * 生成任务ID
   */
//    private String generateTaskId() {
//        return "WT" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
//    }
}
