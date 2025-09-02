package com.rainbow.scheduler.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.scheduler.entity
 * @Filename：TaskConfigParams
 * @Describe:
 */
@Data
@Entity
@Table(name = "task_config_params")
@EqualsAndHashCode(callSuper = true)
@org.hibernate.annotations.Table(appliesTo = "task_config_params", comment = "任务配置参数")
@JsonIgnoreProperties({"fcd","fcu", "lcd", "lcu"})
public class TaskConfigParams extends BaseEntity {

  @Id
  @Column(nullable = false, length = 20)
  @Schema(title = "主键", type = "String")
  private String id;

  @Column(columnDefinition = " varchar(20)  COMMENT '任务配置ID'")
  @Schema(title = "任务配置主键", type = "String")
  private String configId;


  @Column(columnDefinition = " longtext  COMMENT '参数'")
  @Schema(title = "请求参数(JSON格式)", type = "String")
  private String params;


  @Column(columnDefinition = " int(11) default 0  COMMENT '显示顺序'")
  @NotNull(message = "显示顺序不能为空")
  @Schema(title = "显示顺序",type = "Integer")
  @OrderBy(value = Sort.Direction.ASC,INDEX="100")
  private Integer orderNum;

}
