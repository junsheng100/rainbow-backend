package com.rainbow.plans.service;

import java.util.List;

import com.rainbow.base.service.BaseService;
import com.rainbow.plans.entity.WorkPlanTask;
import com.rainbow.plans.entity.WorkTaskDependency;
import com.rainbow.plans.model.response.DependencyResponse;
import com.rainbow.plans.model.response.PERTAnalysisResult;

/**
 * 工序流程依赖管理服务接口 - 合并PERT计算功能
 * 包含任务依赖关系管理和PERT/CPM关键路径计算功能
 *
 * @Author：QQ:304299340
 * @Package：com.rainbow.plans.service
 * @Filename：WorkTaskDependencyService
 * @Date：2025/9/26 14:20
 * @Describe: 工序流程和PERT计算一体化服务
 */
public interface WorkTaskDependencyService extends BaseService<WorkTaskDependency, String> {

  /**
   * 计算关键路径
   *
   * @param planId 计划ID
   * @return 关键路径任务列表
   */
  List<WorkPlanTask> calculateCriticalPath(String planId);

  /**
   * 计算任务的最早和最晚时间
   *
   * @param planId 计划ID
   */
  void calculateTaskTimes(String planId);

  /**
   * 获取PERT分析结果
   *
   * @param planId 计划ID
   * @return PERT分析结果
   */
  PERTAnalysisResult getPERTAnalysis(String planId);

  /**
   * 验证依赖关系
   *
   * @param planId 计划ID
   * @return 是否有效
   */
  boolean validateDependencies(String planId);

  /**
   * 检查循环依赖
   *
   * @param planId 计划ID
   * @return 是否存在循环依赖
   */
  boolean hasCircularDependency(String planId);

  /**
   * 创建工序依赖关系
   *
   * @param dependency 依赖关系
   * @return 创建的依赖关系
   */
  WorkTaskDependency saveDependency(WorkTaskDependency dependency);

  /**
   * 删除工序依赖关系
   *
   * @param dependencyId 依赖关系ID
   */
  void deleteDependency(String dependencyId);

  /**
   * 获取计划的所有依赖关系
   *
   * @param planId 计划ID
   * @return 依赖关系列表
   */
  List<WorkTaskDependency> getDependenciesByPlanId(String planId);

  /**
   * 重新计算计划的所有PERT数据
   *
   * @param planId 计划ID
   */
  void recalculatePlan(String planId);

  boolean validateDependencies(String planId, String prevTaskId, String postTaskId);

  List<DependencyResponse> getPertCpmByPlanId(String planId);


}
