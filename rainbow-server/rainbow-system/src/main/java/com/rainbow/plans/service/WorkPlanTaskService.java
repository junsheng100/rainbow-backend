package com.rainbow.plans.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.plans.entity.WorkPlanTask;
import com.rainbow.plans.enums.TaskStatus;
import com.rainbow.plans.model.request.WorkTaskRequest;

import java.math.BigDecimal;
import java.util.List;

/**
 * 计划任务服务接口
 *
 * @author rainbow
 * @since 2024-01-01
 */
public interface WorkPlanTaskService extends BaseService<WorkPlanTask, String> {

  /**
   * 根据计划ID查询任务
   *
   * @param planId 计划ID
   * @return 任务列表
   */
  List<WorkPlanTask> getTasksByPlanId(String planId);


  /**
   * 更新任务进度
   *
   * @param taskId   任务ID
   * @param progress 进度百分比
   */
  void updateTaskProgress(String taskId, BigDecimal progress);

  /**
   * 分配任务
   *
   * @param taskId     任务ID
   * @param assigneeId 分配人ID
   */
  void assignTask(String taskId, String assigneeId);

  /**
   * 任务状态流转
   *
   * @param taskId 任务ID
   * @param status 目标状态
   */
  void changeTaskStatus(String taskId, TaskStatus status);

  /**
   * 获取里程碑任务
   *
   * @param planId 计划ID
   * @return 里程碑任务列表
   */
  List<WorkPlanTask> getMilestoneTasks(String planId);

  /**
   * 计算计划任务完成率
   *
   * @param planId 计划ID
   * @return 完成率
   */
  double getTaskCompletionRate(String planId);

  /**
   * 获取用户任务
   *
   * @param userId 用户ID
   * @return 任务列表
   */
  List<WorkPlanTask> getUserTasks(String userId);

  /**
   * 批量更新任务排序
   *
   * @param taskIds 任务ID列表（按排序顺序）
   */
  void updateTaskOrder(List<String> taskIds);

  List<WorkPlanTask> findTaskByPlanId(String planId);

  List<WorkPlanTask> calculateCriticalPath(String planId);
}
