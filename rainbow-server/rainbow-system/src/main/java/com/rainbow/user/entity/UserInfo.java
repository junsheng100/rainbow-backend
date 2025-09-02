package com.rainbow.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sys_user_info")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu"})
public class UserInfo extends BaseEntity {

  @Id
  @Schema(title = "用户ID", type = "String")
  @Column(length = 36)
  private String userId;

  @Schema(title = "用户名", type = "String")
  @Column(nullable = false, length = 50)
  private String userName;

  @Schema(title = "头像", type = "String")
  @Column(length = 256)
  private String avatar;

  @Schema(title = "密码", type = "String")
  @Transient
  private String password;


  @Schema(title = "昵称", type = "String")
  @Column(length = 50)
  private String nickname;

  @Schema(title = "用户类型", type = "String")
  @Column(columnDefinition = "varchar(10) default 'COMMON'")
  private String userType;


  @Schema(title = "邮箱", type = "String")
  @Column(length = 200)
  private String email;

  @Schema(title = "手机", type = "String")
  @Column(length = 12)
  private String phone;

  @Column(length = 20)
  @Schema(title = "部门ID", type = "Long")
  private Long deptId;


  @Column(columnDefinition = "varchar(1) default '0' ")
  @Schema(title = "状态（0:可用,1:禁用）",type = "String")
  private String disabled;

  @Transient
  @Schema(title = "部门名称", type = "String")
  private String deptName;


  @Transient
  @Schema(title = "岗位ID", type = "List<Long")
  private List<Long> postIdList;


  @Transient
  @Schema(title = "岗位名称", type = "String")
  private List<String> postNameList;


  @Transient
  @Schema(title = "角色ID", type = "List<Long")
  private List<Long> roleIdList;


  @Transient
  @Schema(title = "角色", type = "String")
  private List<String> roleNameList;


  @Schema(title = "上次登录时间", type = "LocalDateTime")
  @Column
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime login;

  @Schema(title = "上次登出时间", type = "LocalDateTime")
  @Column
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime logout;


  @Transient
  private String keyword;

} 