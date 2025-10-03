package com.rainbow.plans.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.Keyword;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.Search;
import com.rainbow.base.annotation.SearchFilter;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.SearchEnum;
import com.rainbow.plans.enums.Priority;
import com.rainbow.plans.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 计划任务实体
 *
 * @author rainbow
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "work_plan_task")
@org.hibernate.annotations.Table(appliesTo = "work_plan_task", comment = "计划任务表")
@SearchFilter(@Keyword(key = "status", value = "0"))
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu","status"})
public class WorkPlanTask extends BaseEntity {

  @Id
  @Column(length = 20)
  @Schema(title = "任务ID", type = "String")
  private String id;

  @NotBlank(message = "计划ID不能为空")
  @Column(name = "plan_id", length = 20)
  @Schema(title = "计划ID", type = "String")
  private String planId;

  @NotBlank(message = "任务名称不能为空")
  @Search(SELECT = SearchEnum.LIKE)
  @Column(name = "task_name", length = 200)
  @Schema(title = "任务名称", type = "String")
  private String taskName;

  @Column(columnDefinition = "TEXT")
  @Schema(title = "任务描述", type = "String")
  private String taskDesc;

  @Enumerated(EnumType.STRING)
  @Column(length = 10)
  @Schema(title = "优先级", type = "String")
  private Priority priority;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  @Schema(title = "状态", type = "String")
  private TaskStatus taskStatus;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @Column(name = "start_date")
  @Schema(title = "开始日期", type = "LocalDate")
  private LocalDate planStartDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @Column(name = "end_date")
  @Schema(title = "结束日期", type = "LocalDate")
  private LocalDate planEndDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @Column(name = "actual_start_date")
  @Schema(title = "实际开始日期", type = "LocalDate")
  private LocalDate actualStartDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @Column(name = "actual_end_date")
  @Schema(title = "实际结束日期", type = "LocalDate")
  private LocalDate actualEndDate;

  @Column(name = "progress", precision = 5, scale = 2 )
  @Schema(title = "完成进度", type = "BigDecimal")
  private BigDecimal progress;

  @Column(name = "assignee_id", length = 36)
  @Schema(title = "分配人ID", type = "String")
  private String assigneeId;

  @Column(name = "estimated_hours", precision = 8, scale = 2)
  @Schema(title = "预估工时", type = "BigDecimal")
  private BigDecimal estimatedHours;

  @Column(name = "actual_hours", precision = 8, scale = 2)
  @Schema(title = "实际工时", type = "BigDecimal")
  private BigDecimal actualHours;

  @Column(name = "parent_task_id", length = 20)
  @Schema(title = "父任务ID", type = "String")
  private String parentTaskId;

  @OrderBy(value = Sort.Direction.ASC)
  @Column(name = "sort_order")
  @Schema(title = "排序", type = "Integer")
  private Integer sortOrder;

  @Column(name = "is_milestone")
  @Schema(title = "是否里程碑", type = "Boolean")
  private Boolean isMilestone;

  // PERT/CPM相关字段
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Column(name = "early_start_date")
  @Schema(title = "最早开始时间", type = "LocalDate")
  private LocalDate earlyStartDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @Column(name = "early_finish_date")
  @Schema(title = "最早完成时间", type = "LocalDate")
  private LocalDate earlyFinishDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @Column(name = "late_start_date")
  @Schema(title = "最晚开始时间", type = "LocalDate")
  private LocalDate lateStartDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @Column(name = "late_finish_date")
  @Schema(title = "最晚完成时间", type = "LocalDate")
  private LocalDate lateFinishDate;

  @Column(name = "float_days")
  @Schema(title = "浮动时间（天）", type = "Integer")
  private Integer floatDays;

  @Column(name = "is_critical_path")
  @Schema(title = "是否在关键路径上", type = "Boolean")
  private Boolean isCriticalPath;

  @Column(name = "estimated_duration")
  @Schema(title = "预估工期（天）", type = "Integer")
  private Integer estimatedDuration;

  @Column(name = "actual_duration")
  @Schema(title = "实际工期（天）", type = "Integer")
  private Integer actualDuration;
  
  // 关联字段
  @Transient
  @Schema(title = "分配人姓名", type = "String")
  private String assigneeName;

  @Transient
  @Schema(title = "参与者列表", type = "List")
  private List<WorkTaskParticipant> participants;

  @Transient
  @Schema(title = "子任务列表", type = "List")
  private List<WorkPlanTask> children;

  @Transient
  @Schema(title = "依赖关系列表", type = "List")
  private List<WorkTaskDependency> dependencies;

//  public WorkPlanTask() {
//    this.progress = BigDecimal.ZERO;
//    this.estimatedHours = BigDecimal.ZERO;
//    this.actualHours = BigDecimal.ZERO;
//    this.sortOrder = 0;
//    this.isMilestone = false;
//    this.floatDays = 0;
//    this.isCriticalPath = false;
//    this.estimatedDuration = 1;
//    this.actualDuration = 0;
//  }


}
