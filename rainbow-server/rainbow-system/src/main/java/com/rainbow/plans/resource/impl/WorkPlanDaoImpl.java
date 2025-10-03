package com.rainbow.plans.resource.impl;

import com.rainbow.base.exception.BizException;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.plans.entity.WorkPlan;
import com.rainbow.plans.enums.PlanStatus;
import com.rainbow.plans.enums.PlanType;
import com.rainbow.plans.resource.WorkPlanDao;
import com.rainbow.plans.repository.WorkPlanRepository;
import com.rainbow.user.entity.DeptInfo;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.repository.DeptInfoRepository;
import com.rainbow.user.repository.UserInfoRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工作计划数据访问实现
 *
 * @author rainbow
 */
@Slf4j
@Component
public class WorkPlanDaoImpl extends BaseDaoImpl<WorkPlan, String, WorkPlanRepository> implements WorkPlanDao {

  @Autowired
  private UserInfoRepository userInfoRepository;
  @Autowired
  private DeptInfoRepository deptInfoRepository;


  @SneakyThrows
  public WorkPlan check(@Valid WorkPlan entity) {
    WorkPlan old = getOne(entity);
    if (valid(entity)) {
      return old;
    }
    return null;
  }


  @Override
  public List<WorkPlan> findByCreatorId(String creatorId) {
    return jpaRepository.findByCreatorId(creatorId);
  }

  @Override
  public List<WorkPlan> findByOwnerId(String ownerId) {
    return jpaRepository.findByOwnerId(ownerId);
  }

  @Override
  public List<WorkPlan> findByDeptId(Long deptId) {
    return jpaRepository.findByDeptId(deptId);
  }

  @Override
  public List<WorkPlan> findByPlanType(PlanType planType) {
    return jpaRepository.findByPlanType(planType);
  }

  @Override
  public List<WorkPlan> findByStatus(PlanStatus status) {
    return jpaRepository.findByPlanStatus(status);
  }

  @Override
  public List<WorkPlan> findByUserId(String userId) {
    return jpaRepository.findByUserId(userId);
  }

  @Override
  public List<WorkPlan> findByUserIdAndPlanType(String userId, PlanType planType) {
    return jpaRepository.findByUserIdAndPlanType(userId, planType);
  }


  @Override
  public List<WorkPlan> findByProjectId(String projectId) {
    return jpaRepository.findByProjectId(projectId);
  }

  @Override
  public List<WorkPlan> findExpiringPlans(LocalDate date, int days) {
    LocalDate endDate = date.plusDays(days);
    return jpaRepository.findExpiringPlans(date, endDate);
  }

  @Override
  public List<WorkPlan> findDelayedPlans() {
    return jpaRepository.findDelayedPlans();
  }

  @Override
  public List<WorkPlan> findByTemplateId(String templateId) {
    return jpaRepository.findByTemplateId(templateId);
  }

  @Override
  public long countByCreatorId(String creatorId) {
    return jpaRepository.countByCreatorId(creatorId);
  }

  @Override
  public long countByDeptId(Long deptId) {
    return jpaRepository.countByDeptId(deptId);
  }

  @Override
  public double getCompletionRate(String userId, LocalDate startDate, LocalDate endDate) {
    Double result = jpaRepository.getCompletionRate(userId, startDate, endDate);
    return result != null ? result : 0.0;
  }

  @Override
  public void convertData(WorkPlan entity) {
    List<String> userIds = getUserIdList(entity);

    String createId = entity.getCreatorId();
    String ownerId = entity.getOwnerId();
    Long deptId = entity.getDeptId();
    DeptInfo deptInfo = null == deptId ? null : deptInfoRepository.getOne(deptId);
    entity.setDeptName(null == deptInfo ? "" : deptInfo.getDeptName());

    if (CollectionUtils.isNotEmpty(userIds)) {
      List<UserInfo> userInfoList = userInfoRepository.findInUserId(userIds);
      Map<String, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getUserId, u -> u));

      if (StringUtils.isNotBlank(createId)) {
        UserInfo userInfo = MapUtils.getObject(userInfoMap, createId, null);
        entity.setCreatorName(null == userInfo ? "" : userInfo.getNickname());
      }
      if (StringUtils.isNotBlank(ownerId)) {
        UserInfo userInfo = MapUtils.getObject(userInfoMap, ownerId, null);
        entity.setOwnerName(null == userInfo ? "" : userInfo.getNickname());
        if (StringUtils.isBlank(entity.getDeptName())) {
          if (null != userInfo.getDeptId()) {
            deptInfo = deptInfoRepository.getOne(userInfo.getDeptId());
            entity.setDeptName(null == deptInfo ? "" : deptInfo.getDeptName());
            entity.setDeptId(userInfo.getDeptId());
          }
        }
      }
    }
  }

  @Override
  public void convertCollection(List<WorkPlan> list) {
    if (CollectionUtils.isEmpty(list))
      return;

    Map<Long, DeptInfo> deptInfoMap = getDeptInfoMap(list);
    Map<String, UserInfo> userInfoMap = getUserInfoMap(list);


    list.forEach(entity -> {
      String createId = entity.getCreatorId();
      String ownerId = entity.getOwnerId();
      Long deptId = entity.getDeptId();

      entity.setDeptName(null == deptId ? "" : MapUtils.getObject(deptInfoMap, deptId, null).getDeptName());
      if (StringUtils.isNotBlank(createId)) {
        UserInfo userInfo = MapUtils.getObject(userInfoMap, createId, null);
        entity.setCreatorName(null == userInfo ? "" : userInfo.getNickname());
      }
      if (StringUtils.isNotBlank(ownerId)) {
        UserInfo userInfo = MapUtils.getObject(userInfoMap, ownerId, null);
        entity.setOwnerName(null == userInfo ? "" : userInfo.getNickname());
        if (StringUtils.isBlank(entity.getDeptName())) {
          entity.setDeptName(null == userInfo ? "" : userInfo.getDeptName());
          entity.setDeptId(null == userInfo ? null : userInfo.getDeptId());
        }
      }
    });

  }

  @Override
  public boolean valid(@Valid WorkPlan entity) {
    if (null == entity)
      throw new BizException("数据不能为空");
    LocalDate planStartDate = entity.getPlanStartDate();
    LocalDate planEndDate = entity.getPlanEndDate();
    LocalDate actualStartDate = entity.getActualStartDate();
    LocalDate actualEndDate = entity.getActualEndDate();


    if (null != planStartDate) {

      if (null != planEndDate) {
        if (planEndDate.isBefore(planStartDate))
          throw new BizException("计划开始时间不能大于计划结束时间");
      }

      if (null != actualStartDate) {
        if (actualStartDate.isBefore(planStartDate))
          throw new BizException("实际开始时间不能小于计划开始时间");
        if(null != actualEndDate) {
          if (actualEndDate.isBefore(actualStartDate))
            throw new BizException("实际开始时间不能小于当前时间");
        }
      }

      if (null != actualEndDate) {
        if (null != actualStartDate) {
          if (actualEndDate.isBefore(planStartDate)) {
            throw new BizException("实际结束时间不能小于计划开始时间");
          }
        }
      }
    }


    return true;
  }



  private Map<Long, DeptInfo> getDeptInfoMap(List<WorkPlan> list) {
    Map<Long, DeptInfo> deptInfoMap = new HashMap<>();
    if (CollectionUtils.isNotEmpty(list)) {
      List<Long> deptIdList = list.stream().filter(t -> null != t.getDeptId()).map(WorkPlan::getDeptId).collect(Collectors.toList());
      if (CollectionUtils.isNotEmpty(deptIdList)) {
        List<DeptInfo> deptInfoList = deptInfoRepository.findInId(deptIdList);
        deptInfoMap = deptInfoList.stream().collect(Collectors.toMap(DeptInfo::getDeptId, d -> d));
      }
    }
    return deptInfoMap;
  }

  private Map<String, UserInfo> getUserInfoMap(List<WorkPlan> list) {
    Map<String, UserInfo> userInfoMap = new HashMap<>();
    List<String> userIdList = list.stream().flatMap(e -> getUserIdList(e).stream()).collect(Collectors.toList());
    if (CollectionUtils.isNotEmpty(userIdList)) {
      List<UserInfo> userInfoList = userInfoRepository.findInUserId(userIdList);
      List<Long> deptIdList = userInfoList.stream().map(UserInfo::getDeptId).collect(Collectors.toList());

      List<DeptInfo> deptInfoList = CollectionUtils.isEmpty(deptIdList) ? null : deptInfoRepository.findInId(deptIdList);
      Map<Long, DeptInfo> deptInfoMap = CollectionUtils.isEmpty(deptIdList) ? new HashMap<>() : deptInfoList.stream().collect(Collectors.toMap(DeptInfo::getDeptId, d -> d));
      userInfoMap = userInfoList.stream().map(u -> {
        u.setDeptName(null == u.getDeptId() ? "" : MapUtils.getObject(deptInfoMap, u.getDeptId(), null).getDeptName());
        return u;
      }).collect(Collectors.toMap(UserInfo::getUserId, u -> u));
    }

    return userInfoMap;
  }

  private List<String> getUserIdList(WorkPlan entity) {
    List<String> userIds = new ArrayList<>();
    String createId = entity.getCreatorId();
    String ownerId = entity.getOwnerId();

    if (StringUtils.isNotEmpty(createId)) {
      userIds.add(createId);
    }
    if (StringUtils.isNotEmpty(ownerId)) {
      userIds.add(ownerId);
    }

    return userIds;
  }


}
