package com.rainbow.plans.resource.impl;

import java.util.List;

import com.rainbow.base.exception.DataException;
import com.rainbow.plans.enums.DependencyType;
import org.springframework.stereotype.Component;

import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.plans.entity.WorkTaskDependency;
import com.rainbow.plans.repository.WorkTaskDependencyRepository;
import com.rainbow.plans.resource.WorkTaskDependencyDao;

import lombok.extern.slf4j.Slf4j;

/**
 * 工序流程数据访问层实现 - 重新设计的任务前置依赖管理
 *
 * @Author：QQ:304299340
 * @Package：com.rainbow.plans.resource.impl
 * @Filename：WorkTaskDependencyDaoImpl
 * @Date：2025/9/24 12:11
 * @Describe: 支持新的工序流程数据模型
 */
@Slf4j
@Component
public class WorkTaskDependencyDaoImpl extends BaseDaoImpl<WorkTaskDependency, String, WorkTaskDependencyRepository> implements WorkTaskDependencyDao {


  public WorkTaskDependency check(WorkTaskDependency entity) {
    WorkTaskDependency old = getOne(entity);
    DependencyType dependencyType = entity.getDependencyType();
    if (null == dependencyType)
      throw new DataException(" type is null ");
    if (null == old){
      initData(entity);
    }

    return old;
  }

  private void initData(WorkTaskDependency entity) {
    if (null == entity.getDependencyType())
      entity.setDependencyType(DependencyType.FS);
    if (null == entity.getLagDays())
      entity.setLagDays(0);

    if (null == entity.getSequenceOrder()){
      Integer sequenceOrder = jpaRepository.getMaxSequenceOrderByTaskId(entity.getPostTaskId())+1;
      entity.setSequenceOrder(sequenceOrder);
    }

    if (null == entity.getIsCriticalPath())
      entity.setIsCriticalPath(false);
  }


  @Override
  public List<WorkTaskDependency> findByPlanId(String planId) {
    return StringUtils.isBlank(planId) ? null : jpaRepository.findByPlanId(planId);
  }

  @Override
  public List<WorkTaskDependency> findByPostTaskId(String postTaskId) {
    return StringUtils.isBlank(postTaskId) ? null : jpaRepository.findByPostTaskId(postTaskId);
  }

  @Override
  public List<WorkTaskDependency> findByPrevTaskId(String prevTaskId) {
    return StringUtils.isBlank(prevTaskId) ? null : jpaRepository.findByPrevTaskId(prevTaskId);
  }

  @Override
  public boolean existsByPostTaskIdAndPrevTaskId(String postTaskId, String prevTaskId) {
    if (StringUtils.isBlank(postTaskId) || StringUtils.isBlank(prevTaskId)) {
      return false;
    }
    return jpaRepository.existsByPostTaskIdAndPrevTaskId(postTaskId, prevTaskId);
  }

  @Override
  public Integer getMaxSequenceOrderByTaskId(String postTaskId) {
    if (StringUtils.isBlank(postTaskId)) {
      return 0;
    }
    Integer maxOrder = jpaRepository.getMaxSequenceOrderByTaskId(postTaskId);
    return maxOrder != null ? maxOrder : 0;
  }

  @Override
  public List<WorkTaskDependency> findByPlanIdOrderBySequence(String planId) {
    return StringUtils.isBlank(planId) ? null : jpaRepository.findByPlanIdOrderBySequence(planId);
  }

  @Override
  @Deprecated
  public boolean existsDependency(String planId, String prevTaskId, String postTaskId) {
    if (StringUtils.isBlank(planId) || StringUtils.isBlank(prevTaskId) || StringUtils.isBlank(postTaskId)) {
      return false;
    }
    return jpaRepository.existsDependency(planId, prevTaskId, postTaskId);
  }


}
