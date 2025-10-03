package com.rainbow.plans.resource;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.plans.entity.WorkTaskDependency;

/**
 * 工序流程数据访问层 - 重新设计的任务前置依赖管理
 * 
 * @Author：QQ:304299340
 * @Package：com.rainbow.plans.resource
 * @Filename：WorkTaskDependencyDao
 * @Date：2025/9/24 12:10
 * @Describe: 支持新的工序流程数据模型
 */
public interface WorkTaskDependencyDao extends BaseDao<WorkTaskDependency,String> {
  
  /**
   * 根据计划ID查找所有依赖关系
   */
  List<WorkTaskDependency> findByPlanId(String planId);
  
  /**
   * 根据任务ID查找其前置依赖
   */
  List<WorkTaskDependency> findByPostTaskId(String taskId);
  
  /**
   * 根据前置任务ID查找所有依赖于它的任务
   */
  List<WorkTaskDependency> findByPrevTaskId(String prerequisiteTaskId);
  
  /**
   * 检查任务与前置任务之间是否已存在依赖关系
   */
  boolean existsByPostTaskIdAndPrevTaskId(
          @NotBlank(message = "任务ID不能为空") String taskId, 
          @NotBlank(message = "前置任务ID不能为空") String prerequisiteTaskId);
  
  /**
   * 获取任务的最大工序号
   */
  Integer getMaxSequenceOrderByTaskId(String taskId);
  
  /**
   * 根据计划ID和工序号查找依赖关系
   */
  List<WorkTaskDependency> findByPlanIdOrderBySequence(String planId);
  
  /**
   * 旧方法兼容，将被弃用
   * @deprecated 使用 existsByTaskIdAndPrerequisiteTaskId 替代
   */
  @Deprecated
  boolean existsDependency(
          @NotBlank(message = "计划ID不能为空") String planId, 
          @NotBlank(message = "前置任务ID不能为空") String predecessorTaskId, 
          @NotBlank(message = "后续任务ID不能为空") String successorTaskId);
}
