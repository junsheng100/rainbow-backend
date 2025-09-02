package com.rainbow.scheduler.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.util.Date;

/**
 * 任务执行日志实体
 */
@Data
@Entity
@Table(name = "task_log")
@EqualsAndHashCode(callSuper = true)
@org.hibernate.annotations.Table(appliesTo = "task_log", comment = "任务执行日志")
@JsonIgnoreProperties({"fcd", "fcu", "lcd", "lcu"})
public class TaskLog extends BaseEntity {

  @Id
  @Column(nullable = false, length = 20)
  @Schema(title = "主键", type = "String")
  private String id;


  @UnionKey
  @NotNull(message = "任务ID不能为空")
  @Column(length = 20)
  @Schema(title = "任务ID", type = "String")
  private String taskId;

  @Transient
  @Schema(title = "任务名", type = "String")
  private String taskName;

  @UnionKey
  @Column
  @OrderBy(value = Sort.Direction.DESC)
  @NotNull(message = "执行时间不能为空")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Schema(title = "执行时间", type = "Date")
  private Date execTime;

  @Schema(title = "执行结果(0:成功 1:失败)", type = "Integer")
  @Column(name = "exec_result")
  private Integer execResult;

  @Schema(title = "错误信息", type = "String")
  @Column(name = "error_message", columnDefinition = "longtext")
  private String errorMessage;

  @Schema(title = "执行时长(毫秒)", type = "Long")
  @Column(name = "exec_duration")
  private Long execDuration;


} 