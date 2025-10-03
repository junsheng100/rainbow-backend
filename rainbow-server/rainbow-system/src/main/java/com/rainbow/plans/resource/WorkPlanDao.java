package com.rainbow.plans.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.plans.entity.WorkPlan;
import com.rainbow.plans.enums.PlanStatus;
import com.rainbow.plans.enums.PlanType;

import java.time.LocalDate;
import java.util.List;

/**
 * 工作计划数据访问接口
 *
 * @author rainbow
 * @since 2024-01-01
 */
public interface WorkPlanDao extends BaseDao<WorkPlan, String> {

  /**
   * 根据创建者ID查询计划
   */
  List<WorkPlan> findByCreatorId(String creatorId);

  /**
   * 根据负责人ID查询计划
   */
  List<WorkPlan> findByOwnerId(String ownerId);

  /**
   * 根据部门ID查询计划
   */
  List<WorkPlan> findByDeptId(Long deptId);

  /**
   * 根据计划类型查询计划
   */
  List<WorkPlan> findByPlanType(PlanType planType);

  /**
   * 根据状态查询计划
   */
  List<WorkPlan> findByStatus(PlanStatus status);

  /**
   * 根据用户ID查询相关计划（创建者或负责人）
   */
  List<WorkPlan> findByUserId(String userId);

  /**
   * 根据用户ID和计划类型查询计划
   */
  List<WorkPlan> findByUserIdAndPlanType(String userId, PlanType planType);


  /**
   * 根据项目ID查询计划
   */
  List<WorkPlan> findByProjectId(String projectId);

  /**
   * 查询即将到期的计划
   */
  List<WorkPlan> findExpiringPlans(LocalDate date, int days);

  /**
   * 查询延期的计划
   */
  List<WorkPlan> findDelayedPlans();

  /**
   * 根据模板ID查询计划
   */
  List<WorkPlan> findByTemplateId(String templateId);

  /**
   * 统计用户计划数量
   */
  long countByCreatorId(String creatorId);

  /**
   * 统计部门计划数量
   */
  long countByDeptId(Long deptId);

  /**
   * 统计计划完成率
   */
  double getCompletionRate(String userId, LocalDate startDate, LocalDate endDate);

  void convertData(WorkPlan entity);

  void convertCollection(List<WorkPlan> list);


  boolean valid(WorkPlan entity);

}
