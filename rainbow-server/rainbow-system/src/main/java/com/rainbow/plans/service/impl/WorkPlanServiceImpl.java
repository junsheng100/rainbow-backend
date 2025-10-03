package com.rainbow.plans.service.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.base.utils.CommonUtils;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.plans.entity.WorkPlan;
import com.rainbow.plans.entity.WorkPlanParticipant;
import com.rainbow.plans.enums.ParticipantRole;
import com.rainbow.plans.enums.PlanStatus;
import com.rainbow.plans.enums.PlanType;
import com.rainbow.plans.model.request.WorkPlanRequest;
import com.rainbow.plans.resource.WorkPlanDao;
import com.rainbow.plans.resource.WorkPlanParticipantDao;
import com.rainbow.plans.service.WorkPlanService;
import com.rainbow.system.resource.SysConfigDao;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.resource.UserInfoDao;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工作计划服务实现
 *
 * @author rainbow
 * @since 2024-01-01
 */
@Slf4j
@Service
public class WorkPlanServiceImpl extends BaseServiceImpl<WorkPlan, String, WorkPlanDao> implements WorkPlanService {

  @Autowired
  private WorkPlanParticipantDao participantDao;

  @Autowired
  private UserInfoDao userInfoDao;

  @Autowired
  private SysConfigDao configDao;

  @Override
  public WorkPlan createPlan(WorkPlanRequest request) {
    if (null == request)
      throw new BizException("数据不能为空");

    WorkPlan plan = new WorkPlan();
    BeanUtils.copyProperties(request, plan, CommonUtils.getNullPropertyNames(request));
    if (null == plan)
      throw new BizException("计划不能为空");

    List<String> participantIds = request.getParticipantIds();
    return createPlan(plan, participantIds);

  }

  @Override
  public WorkPlan store(@Valid WorkPlan entity) {
    if (validate(entity)) {
      baseDao.store(entity);
      return entity;
    }
    throw new BizException("数据验证失败");
  }


  @Override
  @Transactional
  public WorkPlan createPlan(WorkPlan plan, List<String> participantIds) {
    // 设置默认值
    if (plan.getPlanStatus() == null) {
      plan.setPlanStatus(PlanStatus.DRAFT);
    }
    if (plan.getProgress() == null) {
      plan.setProgress(BigDecimal.ZERO);
    }

    String creatorId = plan.getCreatorId();
    creatorId = StringUtils.isBlank(creatorId) ? jwtTokenUtil.getUserId() : creatorId;
    plan.setCreatorId(creatorId);

    String ownerId =  plan.getOwnerId();
    // 保存计划
    WorkPlan savedPlan = baseDao.store(plan);
    if(StringUtils.isNotBlank(ownerId)){
      WorkPlanParticipant participant = new WorkPlanParticipant(plan.getId(), ownerId, ParticipantRole.OWNER);
      participantDao.store(participant);
    }


    // 添加其他参与者
    if (CollectionUtils.isNotEmpty(participantIds)) {

      for (String userId : participantIds) {
        if (!userId.equals(plan.getCreatorId())) {
          WorkPlanParticipant participant = new WorkPlanParticipant(plan.getId(), userId, ParticipantRole.MEMBER);
          participantDao.store(participant);
        }
      }

    }

    // log.info("创建工作计划成功，计划ID: {}, 创建者: {}", savedPlan.getId(), savedPlan.getCreatorId());
    return savedPlan;
  }

  @Transactional
  @Override
  public WorkPlan editPlan(WorkPlanRequest request) {

    String planId = request.getId();
    if (StringUtils.isBlank(planId))
      throw new BizException("ID is null");

    WorkPlan plan = get(planId);
    if (null == plan)
      throw new BizException("计划不存在");


    BeanUtils.copyProperties(request, plan, CommonUtils.getNullPropertyNames(request));
    plan.setLcd(LocalDateTime.now());

    String creatorId = plan.getCreatorId();
    creatorId = StringUtils.isBlank(creatorId) ? jwtTokenUtil.getUserId() : creatorId;
    plan.setCreatorId(creatorId);

    baseDao.store(plan);

    // 添加或更新创建者为参与者
    List<String> participantIds = request.getParticipantIds();

    if (CollectionUtils.isNotEmpty(participantIds)) {
      List<WorkPlanParticipant> participants = participantIds.stream().map(userId -> {
        return new WorkPlanParticipant(planId, userId, ParticipantRole.MEMBER);
      }).collect(Collectors.toList());

      participantDao.updateByPlanIdAndRole(planId, participants);
    }

    String ownerId =  plan.getOwnerId();

    if (StringUtils.isNotBlank(ownerId)){
      participantDao.deleteByPlanIdAndRole(planId, ParticipantRole.OWNER);
      WorkPlanParticipant participant = new WorkPlanParticipant(plan.getId(), ownerId, ParticipantRole.OWNER);
      participantDao.store(participant);
    }
    return plan;
  }


  @Transactional
  @Override
  public Boolean delete(String id) {
    participantDao.deleteByPlanId(id);
    baseDao.remove(id);
    return true;
  }


  @Override
  @Transactional
  public void updateProgress(String planId, BigDecimal progress) {
    WorkPlan plan = get(planId);
    if (plan == null) {
      throw new BizException("计划不存在");
    }

    if (progress.compareTo(BigDecimal.ZERO) < 0 || progress.compareTo(new BigDecimal("100")) > 0) {
      throw new BizException("进度值必须在0-100之间");
    }

    plan.setProgress(progress);
    plan.setLcd(LocalDateTime.now());

    // 如果进度达到100%，自动设置为已完成状态
    if (progress.compareTo(new BigDecimal("100")) == 0) {
      plan.setPlanStatus(PlanStatus.COMPLETED);
      plan.setActualEndDate(LocalDate.now());
    }

    baseDao.save(plan);
    // log.info("更新计划进度成功，计划ID: {}, 进度: {}%", planId, progress);
  }

  @Override
  @Transactional
  public void assignPlan(String planId, String assigneeId) {
    WorkPlan plan = get(planId);
    if (plan == null) {
      throw new BizException("计划不存在");
    }

    plan.setOwnerId(assigneeId);
    plan.setLcd(LocalDateTime.now());
    baseDao.save(plan);

    // 如果分配人不是参与者，添加为参与者
    if (!participantDao.existsByPlanIdAndUserId(planId, assigneeId)) {
      WorkPlanParticipant participant = new WorkPlanParticipant(
              planId, assigneeId, ParticipantRole.OWNER);
      participantDao.store(participant);
    }

    // log.info("分配计划成功，计划ID: {}, 分配人: {}", planId, assigneeId);
  }

  @Override
  public List<WorkPlan> getUserPlans(String userId, PlanType planType) {
    if (planType == null) {
      return baseDao.findByUserId(userId);
    }
    return baseDao.findByUserIdAndPlanType(userId, planType);
  }

  @Override
  public List<WorkPlan> getDeptPlans(Long deptId) {
    return baseDao.findByDeptId(deptId);
  }

  @Override
  @Transactional
  public void changeStatus(String planId, PlanStatus status, String reason) {
    WorkPlan plan = get(planId);
    if (plan == null) {
      throw new BizException("计划不存在");
    }

    // 检查状态流转是否合法
    if (!plan.getPlanStatus().canTransitionTo(status)) {
      throw new BizException("状态流转不合法，无法从" + plan.getPlanStatus().getDescription() + "流转到" + status.getDescription());
    }

    PlanStatus oldStatus = plan.getPlanStatus();
    plan.setPlanStatus(status);
    plan.setLcd(LocalDateTime.now());

    // 根据状态设置相应的时间
    switch (status) {
      case IN_PROGRESS:
        if (plan.getActualStartDate() == null) {
          plan.setActualStartDate(LocalDate.now());
        }
        break;
      case COMPLETED:
        plan.setActualEndDate(LocalDate.now());
        plan.setProgress(new BigDecimal("100"));
        break;
      case DELAYED:
        if (StringUtils.isNotBlank(reason)) {
          plan.setDelayReason(reason);
        }
        break;
    }

    baseDao.save(plan);
    // log.info("计划状态变更成功，计划ID: {}, 从{}变更为{}", planId, oldStatus.getDescription(), status.getDescription());
  }

  @Override
  @Transactional
  public WorkPlan copyPlan(String planId, String newPlanName) {
    WorkPlan originalPlan = get(planId);
    if (originalPlan == null) {
      throw new BizException("原计划不存在");
    }

    // 创建新计划
    WorkPlan newPlan = new WorkPlan();
    BeanUtils.copyProperties(originalPlan, newPlan, CommonUtils.getNullPropertyNames(originalPlan));
    newPlan.setProgress(BigDecimal.ZERO);

    WorkPlan savedPlan = baseDao.store(newPlan);

    // 复制参与者
    List<WorkPlanParticipant> participants = participantDao.findByPlanId(planId);
    for (WorkPlanParticipant participant : participants) {
      WorkPlanParticipant newParticipant = new WorkPlanParticipant(
              newPlan.getId(), participant.getUserId(), participant.getRole());
      participantDao.store(newParticipant);
    }

    // log.info("复制计划成功，原计划ID: {}, 新计划ID: {}", planId, newPlan.getId());
    return savedPlan;
  }

  @Override
  public PlanStatistics getPlanStatistics(String userId, String timeRange) {
    List<WorkPlan> userPlans = baseDao.findByUserId(userId);

    long totalPlans = userPlans.size();
    long completedPlans = userPlans.stream()
            .filter(plan -> plan.getPlanStatus() == PlanStatus.COMPLETED)
            .count();
    long inProgressPlans = userPlans.stream()
            .filter(plan -> plan.getPlanStatus() == PlanStatus.IN_PROGRESS)
            .count();
    long delayedPlans = userPlans.stream()
            .filter(plan -> plan.getPlanStatus() == PlanStatus.DELAYED)
            .count();

    double completionRate = totalPlans > 0 ? (double) completedPlans / totalPlans * 100 : 0;
    double averageProgress = userPlans.stream()
            .mapToDouble(plan -> plan.getProgress().doubleValue())
            .average()
            .orElse(0.0);

    return new PlanStatistics(totalPlans, completedPlans, inProgressPlans,
            delayedPlans, completionRate, averageProgress);
  }

  @Override
  public List<WorkPlan> getExpiringPlans(int days) {
    return baseDao.findExpiringPlans(LocalDate.now(), days);
  }

  @Override
  public List<WorkPlan> getDelayedPlans() {
    return baseDao.findDelayedPlans();
  }

  @Override
  @Transactional
  public void addParticipant(String planId, String userId, String role) {
    if (participantDao.existsByPlanIdAndUserId(planId, userId)) {
      throw new BizException("用户已经是该计划的参与者");
    }

    ParticipantRole participantRole = ParticipantRole.fromCode(role);
    WorkPlanParticipant participant = new WorkPlanParticipant(planId, userId, participantRole);
    participantDao.store(participant);

    // log.info("添加计划参与者成功，计划ID: {}, 用户ID: {}, 角色: {}", planId, userId, role);
  }

  @Override
  @Transactional
  public void removeParticipant(String planId, String userId) {
    WorkPlanParticipant participant = participantDao.findByPlanIdAndUserId(planId, userId);
    if (participant == null) {
      throw new BizException("用户不是该计划的参与者");
    }

    // 不能移除创建者
    if (participant.getRole() == ParticipantRole.CREATOR) {
      throw new BizException("不能移除计划创建者");
    }

    participantDao.remove(participant.getId());
    // log.info("移除计划参与者成功，计划ID: {}, 用户ID: {}", planId, userId);
  }

  @Override
  public boolean hasPermission(String planId, String userId, String permission) {
    WorkPlanParticipant participant = participantDao.findByPlanIdAndUserId(planId, userId);
    if (participant == null) {
      return false;
    }

    switch (permission) {
      case "VIEW":
        return participant.getRole().hasViewPermission();
      case "EDIT":
        return participant.getRole().hasEditPermission();
      default:
        return false;
    }
  }

  @Override
  public List<UserInfo> findUserByPlanId(String planId) {
    if (StringUtils.isBlank(planId))
      return new ArrayList<>();
    WorkPlan plan = baseDao.get(planId);
    if (null == plan)
      throw new BizException("计划数据不存在");
    List<UserInfo> list = new ArrayList<>();
    List<WorkPlanParticipant> participantList = participantDao.findByPlanId(planId);

    if(CollectionUtils.isNotEmpty(participantList)) {
      List<String> userIdList = participantList.stream().map(WorkPlanParticipant::getUserId).distinct().collect(Collectors.toList());
      list = userInfoDao.findInUserId(userIdList);
    }

    return list;
  }

  @Override
  public String uploadFile(MultipartFile multipartFile, String localPath) {
    File file = null;
    try {
      String filePath = configDao.getFileBasePath() + localPath;
      file = new File(filePath);
      File fdir = file.getParentFile();
      if (!fdir.exists())
        fdir.mkdirs();
      multipartFile.transferTo(file);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return localPath;
  }


  public void convertData(WorkPlan entity) {
    if (null == entity)
      return;
    baseDao.convertData(entity);
    List<WorkPlanParticipant> participantList = participantDao.findByPlanId(entity.getId());
    participantDao.convertCollection(participantList);
    entity.setParticipants(participantList);
  }


  public void convertCollection(List<WorkPlan> list) {
    if (CollectionUtils.isNotEmpty(list)) {
      baseDao.convertCollection(list);
      List<String> plandIdList = list.stream().map(WorkPlan::getId).collect(Collectors.toList());

      List<WorkPlanParticipant> participantList = participantDao.findInPlanId(plandIdList);

      if (CollectionUtils.isNotEmpty(participantList)) {
        participantDao.convertCollection(participantList);
        for (WorkPlan plan : list) {
          List<WorkPlanParticipant> participantData = participantList.stream().filter(participant -> participant.getPlanId().equals(plan.getId())).collect(Collectors.toList());
          plan.setParticipants(participantData);
        }
      }
    }

  }


}
