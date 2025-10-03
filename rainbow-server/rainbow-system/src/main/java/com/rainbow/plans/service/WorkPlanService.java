package com.rainbow.plans.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.plans.entity.WorkPlan;
import com.rainbow.plans.entity.WorkPlanParticipant;
import com.rainbow.plans.enums.PlanStatus;
import com.rainbow.plans.enums.PlanType;
import com.rainbow.plans.model.request.WorkPlanRequest;
import com.rainbow.user.entity.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 工作计划服务接口
 *
 * @author rainbow
 * @since 2024-01-01
 */
public interface WorkPlanService extends BaseService<WorkPlan, String> {


  /**
   * 创建工作计划
   *
   * @param request
   * @return
   */
  WorkPlan createPlan(WorkPlanRequest request);


  /**
   * 创建工作计划
   *
   * @param plan           计划信息
   * @param participantIds 参与者ID列表
   * @return 创建的计划
   */
  WorkPlan createPlan(WorkPlan plan, List<String> participantIds);

  /**
   * 编辑工作计划
   * @param request
   * @return
   */
  WorkPlan editPlan(WorkPlanRequest request);

  /**
   * 更新计划进度
   *
   * @param planId   计划ID
   * @param progress 进度百分比
   */
  void updateProgress(String planId, BigDecimal progress);

  /**
   * 分配计划
   *
   * @param planId     计划ID
   * @param assigneeId 分配人ID
   */
  void assignPlan(String planId, String assigneeId);

  /**
   * 获取用户相关计划
   *
   * @param userId   用户ID
   * @param planType 计划类型
   * @return 计划列表
   */
  List<WorkPlan> getUserPlans(String userId, PlanType planType);

  /**
   * 获取部门计划
   *
   * @param deptId 部门ID
   * @return 计划列表
   */
  List<WorkPlan> getDeptPlans(Long deptId);

  /**
   * 计划状态流转
   *
   * @param planId 计划ID
   * @param status 目标状态
   * @param reason 状态变更原因
   */
  void changeStatus(String planId, PlanStatus status, String reason);

  /**
   * 复制计划
   *
   * @param planId      原计划ID
   * @param newPlanName 新计划名称
   * @return 复制的计划
   */
  WorkPlan copyPlan(String planId, String newPlanName);

  /**
   * 获取计划统计信息
   *
   * @param userId    用户ID
   * @param timeRange 时间范围
   * @return 统计信息
   */
  PlanStatistics getPlanStatistics(String userId, String timeRange);

  /**
   * 获取即将到期的计划
   *
   * @param days 提前天数
   * @return 即将到期的计划列表
   */
  List<WorkPlan> getExpiringPlans(int days);

  /**
   * 获取延期的计划
   *
   * @return 延期的计划列表
   */
  List<WorkPlan> getDelayedPlans();

  /**
   * 添加计划参与者
   *
   * @param planId 计划ID
   * @param userId 用户ID
   * @param role   角色
   */
  void addParticipant(String planId, String userId, String role);

  /**
   * 移除计划参与者
   *
   * @param planId 计划ID
   * @param userId 用户ID
   */
  void removeParticipant(String planId, String userId);

  /**
   * 检查用户是否有计划权限
   *
   * @param planId     计划ID
   * @param userId     用户ID
   * @param permission 权限类型
   * @return 是否有权限
   */
  boolean hasPermission(String planId, String userId, String permission);

  List<UserInfo> findUserByPlanId(String planId);

  String uploadFile(MultipartFile multipartFile, String localPath);


  /**
   * 计划统计信息内部类
   */
  class PlanStatistics {
    private long totalPlans;
    private long completedPlans;
    private long inProgressPlans;
    private long delayedPlans;
    private double completionRate;
    private double averageProgress;

    // 构造函数、getter和setter
    public PlanStatistics() {
    }

    public PlanStatistics(long totalPlans, long completedPlans, long inProgressPlans,
                          long delayedPlans, double completionRate, double averageProgress) {
      this.totalPlans = totalPlans;
      this.completedPlans = completedPlans;
      this.inProgressPlans = inProgressPlans;
      this.delayedPlans = delayedPlans;
      this.completionRate = completionRate;
      this.averageProgress = averageProgress;
    }

    // Getters and Setters
    public long getTotalPlans() {
      return totalPlans;
    }

    public void setTotalPlans(long totalPlans) {
      this.totalPlans = totalPlans;
    }

    public long getCompletedPlans() {
      return completedPlans;
    }

    public void setCompletedPlans(long completedPlans) {
      this.completedPlans = completedPlans;
    }

    public long getInProgressPlans() {
      return inProgressPlans;
    }

    public void setInProgressPlans(long inProgressPlans) {
      this.inProgressPlans = inProgressPlans;
    }

    public long getDelayedPlans() {
      return delayedPlans;
    }

    public void setDelayedPlans(long delayedPlans) {
      this.delayedPlans = delayedPlans;
    }

    public double getCompletionRate() {
      return completionRate;
    }

    public void setCompletionRate(double completionRate) {
      this.completionRate = completionRate;
    }

    public double getAverageProgress() {
      return averageProgress;
    }

    public void setAverageProgress(double averageProgress) {
      this.averageProgress = averageProgress;
    }
  }
}
