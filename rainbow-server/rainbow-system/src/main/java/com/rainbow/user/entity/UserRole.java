package com.rainbow.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_role")
@org.hibernate.annotations.Table(appliesTo = "user_role", comment = "用户-角色")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu"})
public class UserRole extends BaseEntity {

  @Id
  @Schema(title = "ID", type = "Long")
  @Column(length = 20)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Schema(title = "用户ID", type = "String")
  @Column(length = 36)
  private String userId;

  @Schema(title = "角色ID", type = "Long")
  @Column(length = 20)
  private Long roleId;

  @Transient
  private String roleName;


  public UserRole() {

  }

  public UserRole(String userId, Long roleId) {
    this.userId = userId;
    this.roleId = roleId;
  }

  public UserRole(String userId, Long roleId, String fcu, LocalDateTime fcd, String lcu, LocalDateTime lcd, String status) {
    super(fcu, fcd, lcu, lcd, status);
    this.userId = userId;
    this.roleId = roleId;
  }

}
