package com.rainbow.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.Search;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.SearchEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 系统访问记录表 sys_logininfor
 *
 * @author rainvom
 */
@Getter
@Setter
@Entity
@Table(name = "sys_login")
@org.hibernate.annotations.Table(appliesTo = "sys_login", comment = "系统访问记录")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd", "fcu", "lcd", "lcu"})
public class SysLogin extends BaseEntity {

  @Id
  @Column(length = 20)
  @Schema(title = "序号", type = "Long")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long infoId;

  @UnionKey
  @Column(length = 50)
  @Schema(title = "类型 Login/logout", type = "String")
  private String type;

  @UnionKey
  @Column(length = 50)
  @Schema(title = "用户账号", type = "String")
  @Search(SELECT = SearchEnum.LIKE)
  private String userName;

  @Column(length = 50)
  @Schema(title = "用户ID", type = "String")
  private String userId;

  @Column(length = 50)
  @Schema(title = "国家", type = "String")
  private String country;

  @Column(length = 50)
  @Schema(title = "城市", type = "String")
  private String city;

  @Column(length = 50)
  @Schema(title = "省份", type = "String")
  private String pro;

  @Column(length = 50)
  @Schema(title = "ip地址", type = "String")
  private String ipaddr;

  @Column
  @Schema(title = "登录地点", type = "String")
  private String loginLocation;

  @Column(length = 50)
  @Schema(title = "浏览器", type = "String")
  private String browser;

  @Column(length = 50)
  @Schema(title = "操作系统", type = "String")
  private String os;

  @Column
  @Schema(title = "提示消息", type = "String")
  private String msg;

  @UnionKey
  @Column
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Schema(title = "时间", type = "String")
  private LocalDateTime operTime;

  @Transient
  @Search(SELECT = SearchEnum.GREATER_EQ, COLUMN = "operTime")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Schema(title = "开始时间", type = "String")
  private Date startTime;

  @Transient
  @Search(SELECT = SearchEnum.LESS_EQ, COLUMN = "operTime")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Schema(title = "结束时间", type = "String")
  private Date endTime;

}
