package com.rainbow.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "sys_role_menu")
@org.hibernate.annotations.Table(appliesTo = "sys_role_menu", comment = "角色菜单表")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu"})
public class SysRoleMenu extends BaseEntity {


  @Id
  @Schema(title = "ID", type = "Long")
  @Column(length = 20)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @UnionKey
  @Column(length = 20)
  @Schema(title = "角色ID", type = "Long")
  private Long roleId;

  @Transient
  @Schema(title = "角色名称", type = "String")
  private String roleName;

  @UnionKey
  @Column(length = 20)
  @Schema(title = "菜单ID", type = "Long")
  private Long menuId;

  @Transient
  @Schema(title = "菜单名称", type = "String")
  private String menuName;

  public SysRoleMenu() {

  }

  public SysRoleMenu(Long roleId, Long menuId) {
    this.roleId = roleId;
    this.menuId = menuId;
  }

  public SysRoleMenu(Long roleId, Long menuId, String fcu, LocalDateTime fcd, String lcu, LocalDateTime lcd, String status) {
    super(fcu, fcd, lcu, lcd, status);
    this.roleId = roleId;
    this.menuId = menuId;
  }


}
