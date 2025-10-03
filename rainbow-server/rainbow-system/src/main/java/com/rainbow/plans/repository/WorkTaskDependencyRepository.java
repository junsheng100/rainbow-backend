package com.rainbow.plans.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.plans.entity.WorkTaskDependency;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 工序流程数据访问接口 - 重新设计的任务前置依赖管理
 * 
 * @author rainbow
 * @since 2024-01-01
 */
public interface WorkTaskDependencyRepository extends BaseRepository<WorkTaskDependency, String> {

    /**
     * 根据计划ID查询所有工序依赖关系
     */
    @Query("SELECT d FROM WorkTaskDependency d WHERE d.planId = :planId AND d.status = '0' ORDER BY d.sequenceOrder")
    List<WorkTaskDependency> findByPlanId(@Param("planId") String planId);

    /**
     * 根据任务ID查询其前置依赖
     */
    @Query("SELECT d FROM WorkTaskDependency d WHERE d.postTaskId = :postTaskId AND d.status = '0' ORDER BY d.sequenceOrder")
    List<WorkTaskDependency> findByPostTaskId(@Param("postTaskId") String postTaskId);

    /**
     * 根据前置任务ID查询所有依赖于它的任务
     */
    @Query("SELECT d FROM WorkTaskDependency d WHERE d.prevTaskId = :prevTaskId AND d.status = '0' ORDER BY d.sequenceOrder")
    List<WorkTaskDependency> findByPrevTaskId(@Param("prevTaskId") String prevTaskId);

    /**
     * 检查任务与前置任务之间是否已存在依赖关系
     */
    @Query("SELECT COUNT(d) > 0 FROM WorkTaskDependency d WHERE d.postTaskId = :postTaskId AND d.prevTaskId = :prevTaskId AND d.status = '0'")
    boolean existsByPostTaskIdAndPrevTaskId(@Param("postTaskId") String postTaskId, @Param("prevTaskId") String prevTaskId);

    /**
     * 获取任务的最大工序号
     */
    @Query("SELECT COALESCE(MAX(d.sequenceOrder), 0) FROM WorkTaskDependency d WHERE d.postTaskId = :postTaskId AND d.status = '0'")
    Integer getMaxSequenceOrderByTaskId(@Param("postTaskId") String postTaskId);

    /**
     * 根据计划ID查询依赖关系，按工序号排序
     */
    @Query("SELECT d FROM WorkTaskDependency d WHERE d.planId = :planId AND d.status = '0' ORDER BY d.sequenceOrder")
    List<WorkTaskDependency> findByPlanIdOrderBySequence(@Param("planId") String planId);

    /**
     * 检查是否存在循环依赖（新模式）
     */
    @Query("SELECT COUNT(d) > 0 FROM WorkTaskDependency d WHERE d.postTaskId = :postTaskId AND d.prevTaskId = :prevTaskId AND d.status = '0'")
    boolean existsCircularDependencyNewModel(@Param("postTaskId") String postTaskId, @Param("prevTaskId") String prevTaskId);

    // 兼容旧接口，将被弃用
    /**
     * @deprecated 使用 existsDependency 替代
     */
    @Deprecated
    @Query("SELECT COUNT(d) > 0 FROM WorkTaskDependency d WHERE d.planId = :planId AND d.prevTaskId = :prevTaskId AND d.postTaskId = :postTaskId AND d.status = '0'")
    boolean existsDependency(@Param("planId") String planId, @Param("prevTaskId") String prevTaskId, @Param("postTaskId") String postTaskId);
}
