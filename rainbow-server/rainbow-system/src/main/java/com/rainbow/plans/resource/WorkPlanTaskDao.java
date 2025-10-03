package com.rainbow.plans.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.plans.entity.WorkPlanTask;
import com.rainbow.plans.enums.TaskStatus;

import java.util.List;

/**
 * 计划任务数据访问接口
 * 
 * @author rainbow
 * @since 2024-01-01
 */
public interface WorkPlanTaskDao extends BaseDao<WorkPlanTask, String> {

    /**
     * 根据计划ID查询任务
     */
    List<WorkPlanTask> findByPlanId(String planId);

    /**
     * 根据分配人ID查询任务
     */
    List<WorkPlanTask> findByAssigneeId(String assigneeId);

    /**
     * 根据状态查询任务
     */
    List<WorkPlanTask> findByStatus(TaskStatus status);

    /**
     * 根据父任务ID查询子任务
     */
    List<WorkPlanTask> findByParentTaskId(String parentTaskId);

    /**
     * 根据计划ID和状态查询任务
     */
    List<WorkPlanTask> findByPlanIdAndStatus(String planId, TaskStatus status);

    /**
     * 查询里程碑任务
     */
    List<WorkPlanTask> findMilestoneTasks(String planId);

    /**
     * 统计计划任务数量
     */
    long countByPlanId(String planId);

    /**
     * 统计已完成任务数量
     */
    long countCompletedByPlanId(String planId);

    /**
     * 计算计划任务完成率
     */
    double getTaskCompletionRate(String planId);

  List<WorkPlanTask> findCriticalPathTasks(String planId);

  void convertData(WorkPlanTask entity);

  void convertCollection(List<WorkPlanTask> list);

  boolean existsById(String prevTaskId);
}
