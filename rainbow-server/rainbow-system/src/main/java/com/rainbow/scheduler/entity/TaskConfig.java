package com.rainbow.scheduler.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@Entity
@Table(name = "task_config")
@EqualsAndHashCode(callSuper = true)
@org.hibernate.annotations.Table(appliesTo = "task_config", comment = "任务配置")
@JsonIgnoreProperties({"fcd","fcu", "lcd", "lcu"})
public class TaskConfig extends BaseEntity {

  @Id
  @Column(nullable = false, length = 20)
  @Schema(title = "主键", type = "String")
  private String id;

  @NotNull(message = "任务名称不能为空")
  @Column(name = "task_name", nullable = false, length = 100)
  @Schema(title = "任务名称", type = "String")
  private String taskName;

  @UnionKey
  @NotNull(message = "实例类名称不能为空")
  @Column(columnDefinition = " varchar(64) default 0  COMMENT '实例名称'")
  @Schema(title = "类名", type = "String")
  private String beanName;

  @UnionKey
  @Column(columnDefinition = " varchar(64) default 0  COMMENT '方法名称'")
  @Schema(title = "方法名称", type = "String")
  private String methodName;

  @NotNull(message = "Cron表达式不能为空")
  @Column(columnDefinition = " varchar(255) default 0  COMMENT 'Cron表达式'")
  @Schema(title = "Cron表达式", type = "String")
  private String cronExpression;

  @Column(columnDefinition = " int(2) default 0  COMMENT '可用状态'")
  @Schema(title = "可用状态（0:可用,1:禁用）",type = "Integer")
  private  Integer disabled;

  @Column(columnDefinition = " int(2) default 0  COMMENT '运行状态'")
  @Schema(title = "运行状态（0:待运行,1:运行中）",type = "Integer")
  private Integer runStatus;

  @NotNull(message = "显示顺序不能为空")
  @Column(columnDefinition = " int(11) default 0  COMMENT '显示顺序'")
  @Schema(title = "显示顺序",type = "Integer")
  @OrderBy(value = Sort.Direction.ASC,INDEX="100")
  private Integer orderNum;

  @Transient
  private List<TaskConfigParams> params;


}