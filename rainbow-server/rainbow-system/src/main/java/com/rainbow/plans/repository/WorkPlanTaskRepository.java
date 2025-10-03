package com.rainbow.plans.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.plans.entity.WorkPlanTask;
import com.rainbow.plans.enums.TaskStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 计划任务数据访问接口
 * 
 * @author rainbow
 * @since 2024-01-01
 */
public interface WorkPlanTaskRepository extends BaseRepository<WorkPlanTask, String> {

    /**
     * 根据计划ID查询任务
     */
    @Query("SELECT wt FROM WorkPlanTask wt WHERE wt.planId = :planId AND wt.status = '0' ORDER BY wt.sortOrder ASC, wt.fcd ASC")
    List<WorkPlanTask> findByPlanId(@Param("planId") String planId);

    /**
     * 根据分配人ID查询任务
     */
    @Query("SELECT wt FROM WorkPlanTask wt WHERE wt.assigneeId = :assigneeId AND wt.status = '0' ORDER BY wt.fcd DESC")
    List<WorkPlanTask> findByAssigneeId(@Param("assigneeId") String assigneeId);

    /**
     * 根据状态查询任务
     */
    @Query("SELECT wt FROM WorkPlanTask wt WHERE wt.status = :status AND wt.status = '0' ORDER BY wt.fcd DESC")
    List<WorkPlanTask> findByTaskStatus(@Param("status") TaskStatus status);

    /**
     * 根据父任务ID查询子任务
     */
    @Query("SELECT wt FROM WorkPlanTask wt WHERE wt.parentTaskId = :parentTaskId AND wt.status = '0' ORDER BY wt.sortOrder ASC, wt.fcd ASC")
    List<WorkPlanTask> findByParentTaskId(@Param("parentTaskId") String parentTaskId);

    /**
     * 根据计划ID和状态查询任务
     */
    @Query("SELECT wt FROM WorkPlanTask wt WHERE wt.planId = :planId AND wt.status = :status AND wt.status = '0' ORDER BY wt.sortOrder ASC")
    List<WorkPlanTask> findByPlanIdAndStatus(@Param("planId") String planId, @Param("status") TaskStatus status);

    /**
     * 查询里程碑任务
     */
    @Query("SELECT wt FROM WorkPlanTask wt WHERE wt.planId = :planId AND wt.isMilestone = true AND wt.status = '0' ORDER BY wt.sortOrder ASC")
    List<WorkPlanTask> findMilestoneTasks(@Param("planId") String planId);

    /**
     * 统计计划任务数量
     */
    @Query("SELECT COUNT(wt) FROM WorkPlanTask wt WHERE wt.planId = :planId AND wt.status = '0'")
    long countByPlanId(@Param("planId") String planId);

    /**
     * 统计已完成任务数量
     */
    @Query("SELECT COUNT(wt) FROM WorkPlanTask wt WHERE wt.planId = :planId AND wt.status = 'COMPLETED' AND wt.status = '0'")
    long countCompletedByPlanId(@Param("planId") String planId);

    /**
     * 计算计划任务完成率
     */
    @Query("SELECT AVG(wt.progress) FROM WorkPlanTask wt WHERE wt.planId = :planId AND wt.status = '0'")
    Double getTaskCompletionRate(@Param("planId") String planId);





    // PERT/CPM相关查询方法

    /**
     * 查询关键路径任务
     */
    @Query("SELECT wt FROM WorkPlanTask wt WHERE wt.planId = :planId AND wt.isCriticalPath = true AND wt.status = '0' ORDER BY wt.earlyStartDate ASC")
    List<WorkPlanTask> findCriticalPathTasks(@Param("planId") String planId);


}
