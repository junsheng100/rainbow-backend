package com.rainbow.plans.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.Keyword;
import com.rainbow.base.annotation.SearchFilter;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.plans.enums.ParticipantRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 计划参与者实体
 *
 * @author rainbow
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "work_plan_participant")
@org.hibernate.annotations.Table(appliesTo = "work_plan_participant", comment = "计划参与者表")
@SearchFilter(@Keyword(key = "status", value = "0"))
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd", "fcu", "lcd", "lcu"})
public class WorkPlanParticipant extends BaseEntity {

  @Id
  @Column(length = 20)
  @Schema(title = "ID", type = "String")
  private String id;

  @UnionKey
  @NotBlank(message = "计划ID不能为空")
  @Column(name = "plan_id", length = 20)
  @Schema(title = "计划ID", type = "String")
  private String planId;

  @UnionKey
  @NotBlank(message = "用户ID不能为空")
  @Column(name = "user_id", length = 36)
  @Schema(title = "用户ID", type = "String")
  private String userId;

  @UnionKey
  @NotNull(message = "角色不能为空")
  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  @Schema(title = "角色", type = "String")
  private ParticipantRole role;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Column(name = "join_date")
  @Schema(title = "加入时间", type = "LocalDateTime")
  private LocalDateTime joinDate;

  // 关联字段
  @Transient
  @Schema(title = "用户姓名", type = "String")
  private String userName;

  @Transient
  @Schema(title = "用户头像", type = "String")
  private String userAvatar;

  @Transient
  @Schema(title = "部门名称", type = "String")
  private String deptName;


  public WorkPlanParticipant(){

  }

  public WorkPlanParticipant(String planId, String userId, ParticipantRole role) {
    this.planId = planId;
    this.userId = userId;
    this.role = role;
  }

}
