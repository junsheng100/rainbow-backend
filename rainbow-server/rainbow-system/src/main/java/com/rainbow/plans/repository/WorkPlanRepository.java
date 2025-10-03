package com.rainbow.plans.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.plans.entity.WorkPlan;
import com.rainbow.plans.enums.PlanStatus;
import com.rainbow.plans.enums.PlanType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 工作计划数据访问接口
 * 
 * @author rainbow
 * @since 2024-01-01
 */
public interface WorkPlanRepository extends BaseRepository<WorkPlan, String> {

    /**
     * 根据创建者ID查询计划
     */
    @Query("SELECT wp FROM WorkPlan wp WHERE wp.creatorId = :creatorId AND wp.status = '0' ORDER BY wp.fcd DESC")
    List<WorkPlan> findByCreatorId(@Param("creatorId") String creatorId);

    /**
     * 根据负责人ID查询计划
     */
    @Query("SELECT wp FROM WorkPlan wp WHERE wp.ownerId = :ownerId AND wp.status = '0' ORDER BY wp.fcd DESC")
    List<WorkPlan> findByOwnerId(@Param("ownerId") String ownerId);

    /**
     * 根据部门ID查询计划
     */
    @Query("SELECT wp FROM WorkPlan wp WHERE wp.deptId = :deptId AND wp.status = '0' ORDER BY wp.fcd DESC")
    List<WorkPlan> findByDeptId(@Param("deptId") Long deptId);

    /**
     * 根据计划类型查询计划
     */
    @Query("SELECT wp FROM WorkPlan wp WHERE wp.planType = :planType AND wp.status = '0' ORDER BY wp.fcd DESC")
    List<WorkPlan> findByPlanType(@Param("planType") PlanType planType);

    /**
     * 根据状态查询计划
     */
    @Query("SELECT wp FROM WorkPlan wp WHERE wp.planStatus = :status AND wp.status = '0' ORDER BY wp.fcd DESC")
    List<WorkPlan> findByPlanStatus(@Param("status") PlanStatus status);

    /**
     * 根据用户ID查询相关计划（创建者或负责人）
     */
    @Query("SELECT wp FROM WorkPlan wp WHERE (wp.creatorId = :userId OR wp.ownerId = :userId) AND wp.status = '0' ORDER BY wp.fcd DESC")
    List<WorkPlan> findByUserId(@Param("userId") String userId);

    /**
     * 根据用户ID和计划类型查询计划
     */
    @Query("SELECT wp FROM WorkPlan wp WHERE (wp.creatorId = :userId OR wp.ownerId = :userId) AND wp.planType = :planType AND wp.status = '0' ORDER BY wp.fcd DESC")
    List<WorkPlan> findByUserIdAndPlanType(@Param("userId") String userId, @Param("planType") PlanType planType);



    /**
     * 根据项目ID查询计划
     */
    @Query("SELECT wp FROM WorkPlan wp WHERE wp.projectId = :projectId AND wp.status = '0' ORDER BY wp.fcd DESC")
    List<WorkPlan> findByProjectId(@Param("projectId") String projectId);

    /**
     * 查询即将到期的计划
     */
    @Query("SELECT wp FROM WorkPlan wp WHERE wp.planEndDate BETWEEN :planStartDate AND :planEndDate AND wp.planStatus IN ('IN_PROGRESS', 'PAUSED') AND wp.status = '0' ORDER BY wp.planEndDate ASC")
    List<WorkPlan> findExpiringPlans(@Param("planStartDate") LocalDate planStartDate, @Param("planEndDate") LocalDate planEndDate);

    /**
     * 查询延期的计划
     */
    @Query("SELECT wp FROM WorkPlan wp WHERE wp.planEndDate < CURRENT_DATE AND wp.planStatus IN ('IN_PROGRESS', 'PAUSED') AND wp.status = '0' ORDER BY wp.planEndDate ASC")
    List<WorkPlan> findDelayedPlans();

    /**
     * 根据模板ID查询计划
     */
    @Query("SELECT wp FROM WorkPlan wp WHERE wp.templateId = :templateId AND wp.status = '0' ORDER BY wp.fcd DESC")
    List<WorkPlan> findByTemplateId(@Param("templateId") String templateId);

    /**
     * 统计用户计划数量
     */
    @Query("SELECT COUNT(wp) FROM WorkPlan wp WHERE wp.creatorId = :creatorId AND wp.status = '0'")
    long countByCreatorId(@Param("creatorId") String creatorId);

    /**
     * 统计部门计划数量
     */
    @Query("SELECT COUNT(wp) FROM WorkPlan wp WHERE wp.deptId = :deptId AND wp.status = '0'")
    long countByDeptId(@Param("deptId") Long deptId);

    /**
     * 统计计划完成率
     */
    @Query("SELECT AVG(wp.progress) FROM WorkPlan wp WHERE (wp.creatorId = :userId OR wp.ownerId = :userId) AND wp.fcd BETWEEN :planStartDate AND :planEndDate AND wp.status = '0'")
    Double getCompletionRate(@Param("userId") String userId, @Param("planStartDate") LocalDate planStartDate, @Param("planEndDate") LocalDate planEndDate);

    /**
     * 根据计划名称模糊查询
     */
    @Query("SELECT wp FROM WorkPlan wp WHERE wp.planName LIKE %:planName% AND wp.status = '0' ORDER BY wp.fcd DESC")
    List<WorkPlan> findByPlanNameLike(@Param("planName") String planName);

    /**
     * 根据优先级查询计划
     */
    @Query("SELECT wp FROM WorkPlan wp WHERE wp.priority = :priority AND wp.status = '0' ORDER BY wp.fcd DESC")
    List<WorkPlan> findByPriority(@Param("priority") String priority);

    /**
     * 查询用户参与的计划（通过参与者表关联）
     */
    @Query("SELECT wp FROM WorkPlan wp JOIN WorkPlanParticipant wpp ON wp.id = wpp.planId WHERE wpp.userId = :userId AND wp.status = '0' ORDER BY wp.fcd DESC")
    List<WorkPlan> findParticipatedPlans(@Param("userId") String userId);

    /**
     * 查询计划统计信息
     */
    @Query("SELECT wp.planStatus, COUNT(wp) FROM WorkPlan wp WHERE (wp.creatorId = :userId OR wp.ownerId = :userId) AND wp.status = '0' GROUP BY wp.planStatus")
    List<Object[]> getPlanStatistics(@Param("userId") String userId);
}
