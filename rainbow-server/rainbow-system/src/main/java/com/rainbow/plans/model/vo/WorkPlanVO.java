package com.rainbow.plans.model.vo;

import com.rainbow.base.utils.CommonUtils;
import com.rainbow.plans.entity.WorkPlan;
import com.rainbow.plans.entity.WorkPlanParticipant;
import com.rainbow.plans.entity.WorkPlanTask;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 工作计划视图对象
 *
 * @author rainbow
 * @since 2024-01-01
 */
@Data
@Schema(title = "工作计划视图对象")
public class WorkPlanVO {

  @Schema(title = "计划ID")
  private String id;

  @Schema(title = "计划名称")
  private String planName;

  @Schema(title = "计划描述")
  private String planDesc;

  @Schema(title = "计划类型")
  private String planType;

  @Schema(title = "优先级")
  private String priority;

  @Schema(title = "状态")
  private String status;

  @Schema(title = "开始日期")
  private LocalDate startDate;

  @Schema(title = "结束日期")
  private LocalDate endDate;

  @Schema(title = "实际开始日期")
  private LocalDate actualStartDate;

  @Schema(title = "实际结束日期")
  private LocalDate actualEndDate;

  @Schema(title = "完成进度")
  private BigDecimal progress;

  @Schema(title = "创建者ID")
  private String creatorId;

  @Schema(title = "创建者姓名")
  private String creatorName;

  @Schema(title = "负责人ID")
  private String ownerId;

  @Schema(title = "负责人姓名")
  private String ownerName;

  @Schema(title = "部门ID")
  private Long deptId;

  @Schema(title = "部门名称")
  private String deptName;

  @Schema(title = "项目ID")
  private String projectId;

  @Schema(title = "预估工时")
  private BigDecimal estimatedHours;

  @Schema(title = "实际工时")
  private BigDecimal actualHours;

  @Schema(title = "延期原因")
  private String delayReason;

  @Schema(title = "完成说明")
  private String completionNote;

  @Schema(title = "创建时间")
  private String createTime;

  @Schema(title = "更新时间")
  private String updateTime;

  @Schema(title = "任务列表")
  private List<WorkPlanTask> tasks;

  @Schema(title = "参与者列表")
  private List<WorkPlanParticipant> participants;

  @Schema(title = "子计划列表")
  private List<WorkPlanVO> children;

  /**
   * 从实体转换为VO
   */
  public static WorkPlanVO fromEntity(WorkPlan plan) {
    WorkPlanVO vo = new WorkPlanVO();
    BeanUtils.copyProperties(plan, vo, CommonUtils.getNullPropertyNames(plan));

    return vo;
  }


}
