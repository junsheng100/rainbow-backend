package com.rainbow.plans.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.*;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.SearchEnum;
import com.rainbow.plans.enums.PlanStatus;
import com.rainbow.plans.enums.PlanType;
import com.rainbow.plans.enums.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 工作计划实体
 *
 * @author rainbow
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "work_plan")
@org.hibernate.annotations.Table(appliesTo = "work_plan", comment = "工作计划表")
@SearchFilter(@Keyword(key = "status", value = "0"))
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd", "fcu", "lcd", "lcu"})
public class WorkPlan extends BaseEntity {

  @Id
  @Column(length = 20)
  @Schema(title = "计划ID", type = "String")
  private String id;

  @UnionKey
  @NotBlank(message = "计划名称不能为空")
  @Search(SELECT = SearchEnum.LIKE)
  @Column(name = "plan_name", length = 200)
  @Schema(title = "计划名称", type = "String")
  private String planName;

  @Column(columnDefinition = "TEXT")
  @Schema(title = "计划描述", type = "String")
  private String planDesc;

  @NotNull(message = "计划类型不能为空")
  @Enumerated(EnumType.STRING)
  @Column(name = "plan_type", length = 20)
  @Schema(title = "计划类型", type = "String")
  private PlanType planType;

  @Enumerated(EnumType.STRING)
  @Column(length = 10)
  @Schema(title = "优先级", type = "String")
  private Priority priority;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  @Schema(title = "状态", type = "String")
  private PlanStatus planStatus;

  @OrderBy(value = Sort.Direction.DESC,INDEX = "100")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Column(name = "start_date")
  @Schema(title = "开始日期", type = "LocalDate")
  private LocalDate planStartDate;

  @OrderBy(value = Sort.Direction.DESC,INDEX = "99")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Column(name = "end_date")
  @Schema(title = "结束日期", type = "LocalDate")
  private LocalDate planEndDate;

  @OrderBy(value = Sort.Direction.DESC,INDEX = "98")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Column(name = "actual_start_date")
  @Schema(title = "实际开始日期", type = "LocalDate")
  private LocalDate actualStartDate;

  @OrderBy(value = Sort.Direction.DESC,INDEX = "97")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Column(name = "actual_end_date")
  @Schema(title = "实际结束日期", type = "LocalDate")
  private LocalDate actualEndDate;

  @Column(name = "progress", precision = 5, scale = 2, columnDefinition = "default 0")
  @Schema(title = "完成进度", type = "BigDecimal")
  private BigDecimal progress;

  @NotBlank(message = "创建者ID不能为空")
  @Column(name = "creator_id", length = 36)
  @Schema(title = "创建者ID", type = "String")
  private String creatorId;

  @Column(name = "owner_id", length = 36)
  @Schema(title = "负责人ID", type = "String")
  private String ownerId;


  @Column(name = "template_id", length = 20)
  @Schema(title = "模板ID", type = "String")
  private String templateId;

  @Column(name = "dept_id", length = 20)
  @Schema(title = "部门ID", type = "Long")
  private Long deptId;

  @Column(name = "project_id", length = 20)
  @Schema(title = "项目ID", type = "String")
  private String projectId;

  @Column(name = "estimated_hours", precision = 8, scale = 2, columnDefinition = "default 0")
  @Schema(title = "预估工时", type = "BigDecimal")
  private BigDecimal estimatedHours;

  @Column(name = "actual_hours", precision = 8, scale = 2, columnDefinition = "default 0")
  @Schema(title = "实际工时", type = "BigDecimal")
  private BigDecimal actualHours;

  @Column(columnDefinition = "TEXT")
  @Schema(title = "延期原因", type = "String")
  private String delayReason;

  @Column(columnDefinition = "TEXT")
  @Schema(title = "完成说明", type = "String")
  private String completionNote;

  @Schema(title = "logo", type = "String")
  @Column(length = 256)
  private String logo;

  // 关联字段
  @Transient
  @Schema(title = "创建者姓名", type = "String")
  private String creatorName;

  @Transient
  @Schema(title = "负责人姓名", type = "String")
  private String ownerName;

  @Transient
  @Schema(title = "部门名称", type = "String")
  private String deptName;

  @Transient
  @Schema(title = "子计划列表", type = "List")
  private List<WorkPlan> children;

  @Transient
  @Schema(title = "任务列表", type = "List")
  private List<WorkPlanTask> tasks;

  @Transient
  @Schema(title = "参与者列表", type = "List")
  private List<WorkPlanParticipant> participants;

  @Transient
  @Search(SELECT = SearchEnum.GREATER_EQ,COLUMN = "planStartDate")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(title = "开始日期", type = "LocalDate")
  private Date startTime;

  @Transient
  @Search(SELECT = SearchEnum.LESS_EQ,COLUMN = "planEndDate")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(title = "结束日期", type = "LocalDate")
  private Date endTime;

}
