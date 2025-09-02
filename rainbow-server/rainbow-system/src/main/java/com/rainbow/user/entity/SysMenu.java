package com.rainbow.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.Search;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.SearchEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单权限表 sys_menu
 *
 * @author rainvom
 */
@Getter
@Setter
@Entity
@Table(name = "sys_menu")
@org.hibernate.annotations.Table(appliesTo = "sys_menu", comment = "菜单权限表")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd", "fcu", "lcd", "lcu"})
public class SysMenu extends BaseEntity {


  @Id
  @Column(length = 20)
  @Schema(title = "菜单ID", type = "Long")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long menuId;


  @Column(length = 50)
  @Schema(title = "菜单名称", type = "String")
  @Search(SELECT = SearchEnum.LIKE)
  @NotBlank(message = "菜单名称不能为空")
  @Size(min = 0, max = 50, message = "菜单名称长度不能超过50个字符")
  private String menuName;

  @Transient
  @Schema(title = "父菜单名称", type = "String")
  private String parentName;


  @Column(length = 20)
  @Schema(title = "父菜单ID", type = "Long")
  @OrderBy(value = Sort.Direction.ASC, INDEX = "2")
  private Long parentId;


  @Column(length = 11)
  @Schema(title = "显示顺序", type = "Integer")
  @OrderBy(value = Sort.Direction.ASC, INDEX = "1")
  @NotNull(message = "显示顺序不能为空")
  private Integer orderNum;


  @Column(length = 200)
  @Schema(title = "路由地址", type = "String")
  @Size(min = 0, max = 200, message = "路由地址不能超过200个字符")
  private String path;


  @Column(length = 512)
  @Schema(title = "请求地址", type = "String")
  @Size(min = 0, max = 512, message = "请求地址")
  private String requestUrl;


  @Column(length = 12)
  @Schema(title = "请求类型", type = "String")
  @Size(min = 0, max = 12, message = "请求类型长度不能超过12个字符")
  private String requestMethod;


  @Column(length = 32)
  @Schema(title = "接口ID", type = "String")
  private String interfaceId;


  @Column
  @Schema(title = "组件路径", type = "String")
  @Size(min = 0, max = 200, message = "组件路径不能超过255个字符")
  private String component;

  @Column
  @Schema(title = "路由参数", type = "String")
  private String query;

  /**
   * 路由名称，默认和路由地址相同的驼峰格式（注意：因为vue3版本的router会删除名称相同路由，为避免名字的冲突，特殊情况可以自定义）
   */
  @Column(length = 50)
  @Schema(title = "路由名称", type = "String")
  private String routeName;

  @Schema(title = "是否为外链（0是 1否）", type = "String")
  @Column(columnDefinition = "varchar(2) default '1'")
  private String isFrame;

  /**
   * 是否缓存（0缓存 1不缓存）
   */
  @Schema(title = "是否缓存（0是 1否）", type = "String")
  @Column(columnDefinition = "varchar(2) default '1'")
  private String isCache;


  @Column(length = 1)
  @Schema(title = "类型枚举 MenuTypeEnum", type = "Integer")
  @NotBlank(message = "菜单类型不能为空")
  private String menuType;

  @Schema(title = "显示状态（0显示 1隐藏）", type = "String")
  @Column(columnDefinition = "varchar(2) default '0'")
  private String visible;

  @Column(columnDefinition = "varchar(2) default '0'")
  @Schema(title = "状态（0:可用,-1:删除）", type = "String")
  private String disabled;

  /**
   *
   */
  @Schema(title = "权限字符串", type = "String")
  @Column(length = 100)
  @Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
  private String perms;

  /**
   * 菜单图标
   */
  @Schema(title = "菜单图标", type = "String")
  @Column(length = 100)
  private String icon;

  @Transient
  private List<SysMenu> children = new ArrayList<SysMenu>();

  public String getPerms() {
    perms = perms == null ? "" : perms;
    return perms;
  }


}
