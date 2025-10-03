package com.rainbow.plans.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rainbow.plans.enums.Priority;
import com.rainbow.plans.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.plans.model.request
 * @Filename：WorkPlanRequest
 * @Date：2025/9/16 20:09
 * @Describe:
 */
@Data
public class WorkTaskRequest implements Serializable {


  @Schema(title = "任务ID", type = "String")
  private String id;

  @NotBlank(message = "计划ID不能为空")
  @Schema(title = "计划ID", type = "String")
  private String planId;

  @NotBlank(message = "任务名称不能为空")
  @Schema(title = "任务名称", type = "String")
  private String taskName;

  @Schema(title = "任务描述", type = "String")
  private String taskDesc;

  @Schema(title = "任务类型", type = "String")
  private String taskType;

  @Enumerated(EnumType.STRING)
  @Schema(title = "优先级", type = "String")
  private Priority priority;

  @Enumerated(EnumType.STRING)
  @Schema(title = "状态", type = "String")
  private TaskStatus taskStatus;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(title = "开始日期", type = "LocalDate")
  private LocalDate startDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(title = "结束日期", type = "LocalDate")
  private LocalDate endDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(title = "实际开始日期", type = "LocalDate")
  private LocalDate actualStartDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(title = "实际结束日期", type = "LocalDate")
  private LocalDate actualEndDate;

  @Column(name = "progress", precision = 5, scale = 2)
  @Schema(title = "完成进度", type = "BigDecimal")
  private BigDecimal progress;

  @Schema(title = "分配人ID", type = "String")
  private String assigneeId;

  @Schema(title = "预估工时", type = "BigDecimal")
  private BigDecimal estimatedHours;

  @Schema(title = "实际工时", type = "BigDecimal")
  private BigDecimal actualHours;

  @Schema(title = "父任务ID", type = "String")
  private String parentTaskId;

  @Schema(title = "排序", type = "Integer")
  private Integer sortOrder;

  @Schema(title = "是否里程碑", type = "Boolean")
  private Boolean isMilestone;

  @Schema(title = "参与者ID", type = " List<String>")
  private List<String> participantIds;

}
