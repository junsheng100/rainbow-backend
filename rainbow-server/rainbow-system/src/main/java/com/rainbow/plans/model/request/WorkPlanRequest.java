package com.rainbow.plans.model.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rainbow.plans.enums.PlanStatus;
import com.rainbow.plans.enums.PlanType;
import com.rainbow.plans.enums.Priority;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.plans.model.request
 * @Filename：WorkPlanRequest
 * @Date：2025/9/16 20:09
 * @Describe:
 */
@Data
public class WorkPlanRequest implements Serializable {


  @Schema(title = "计划ID", type = "String")
  private String id;

  @NotBlank(message = "计划名称不能为空")
  @Schema(title = "计划名称", type = "String")
  private String planName;

  @Schema(title = "计划描述", type = "String")
  private String planDesc;

  @NotNull(message = "计划类型不能为空")
  @Enumerated(EnumType.STRING)
  @Schema(title = "计划类型", type = "String")
  private PlanType planType;

  @Enumerated(EnumType.STRING)
  @Schema(title = "优先级", type = "String")
  private Priority priority;

  @Enumerated(EnumType.STRING)
  @Schema(title = "状态", type = "String")
  private PlanStatus planStatus;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(title = "开始日期", type = "LocalDate")
  private LocalDate planStartDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(title = "结束日期", type = "LocalDate")
  private LocalDate planEndDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(title = "实际开始日期", type = "LocalDate")
  private LocalDate actualStartDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(title = "实际结束日期", type = "LocalDate")
  private LocalDate actualEndDate;

  @Schema(title = "完成进度", type = "BigDecimal")
  private BigDecimal progress;

  @NotBlank(message = "创建者ID不能为空")
  @Schema(title = "创建者ID", type = "String")
  private String creatorId;

  @Schema(title = "负责人ID", type = "String")
  private String ownerId;

  @Schema(title = "父计划ID", type = "String")
  private String parentId;

  @Schema(title = "模板ID", type = "String")
  private String templateId;

  @Schema(title = "部门ID", type = "Long")
  private Long deptId;

  @Schema(title = "项目ID", type = "String")
  private String projectId;

  @Schema(title = "预估工时", type = "BigDecimal")
  private BigDecimal estimatedHours;

  @Schema(title = "实际工时", type = "BigDecimal")
  private BigDecimal actualHours;

  @Schema(title = "延期原因", type = "String")
  private String delayReason;

  @Schema(title = "完成说明", type = "String")
  private String completionNote;

  @Schema(title = "logo路径", type = "String")
  private String logo;

  @Schema(title = "参与者姓名", type = " List<String>")
  private List<String> participantIds;

}
