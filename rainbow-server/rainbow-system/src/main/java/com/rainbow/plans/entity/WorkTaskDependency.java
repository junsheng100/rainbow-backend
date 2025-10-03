package com.rainbow.plans.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.Keyword;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.SearchFilter;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.plans.enums.DependencyType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Sort;

/**
 * 工序流程实体 - 定义任务之间的前置依赖关系
 * 重新设计：取消后续任务概念，采用前置任务依赖模式
 *
 * @author rainbow
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "work_task_dependency")
@org.hibernate.annotations.Table(appliesTo = "work_task_dependency", comment = "工序流程表 - 任务前置依赖关系")
@SearchFilter(@Keyword(key = "status", value = "0"))
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd", "fcu", "lcd", "lcu"})
public class WorkTaskDependency extends BaseEntity {

  @Id
  @Column(length = 20)
  @Schema(title = "依赖关系ID", type = "String")
  private String id;


  @NotBlank(message = "工序流程编码不能为空")
  @Column(length = 20)
  @Schema(title = "工序流程编码", type = "String")
  private String dependencyCode;


  @NotBlank(message = "工序名称不能为空")
  @Column(length = 20)
  @Schema(title = "工序流程编码", type = "String")
  private String name;

  @UnionKey
  @NotBlank(message = "计划ID不能为空")
  @Column(name = "plan_id", length = 20)
  @Schema(title = "计划ID", type = "String")
  private String planId;

  @UnionKey
  @Column(length = 20)
  @Schema(title = "紧前任务ID", type = "String")
  private String prevTaskId;

  @UnionKey
  @Column(length = 20)
  @Schema(title = "紧后任务ID", type = "String")
  private String postTaskId;


  @Enumerated(EnumType.STRING)
  @Column(length = 10)
  @Schema(title = "依赖类型", type = "String")
  private DependencyType dependencyType;

  @Column(name = "lag_days")
  @Schema(title = "滞后天数", type = "Integer")
  private Integer lagDays;

  @Column
  @OrderBy(value = Sort.Direction.ASC)
  @Schema(title = "工序顺序号", type = "Integer")
  private Integer sequenceOrder;

  @Column
  @Schema(title = "是否关键路径", type = "Boolean")
  private Boolean isCriticalPath;

  // 关联字段
  @Transient
  @Schema(title = "后置任务名称", type = "String")
  private String postTaskName;

  @Transient
  @Schema(title = "前置任务名称", type = "String")
  private String prevTaskName;

}
