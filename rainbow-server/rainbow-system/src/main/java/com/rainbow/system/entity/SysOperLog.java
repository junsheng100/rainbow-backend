package com.rainbow.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.Search;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.SearchEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 操作日志记录表 oper_log
 *
 * @author rainvom
 */
@Getter
@Setter
@Entity
@Table(name = "sys_oper_log")
@org.hibernate.annotations.Table(appliesTo = "sys_oper_log", comment = "操作日志记录")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd", "fcu", "lcd", "lcu"})
public class SysOperLog extends BaseEntity {

  @Id
  @Schema(title = "主键", type = "Long")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(length = 20)
  private Long operId;

  @Column(length = 64)
  @Search(SELECT = SearchEnum.LIKE)
  private String title;

  @Transient
  private Integer[] businessTypes;

  @Column(length = 200)
  @Schema(title = "请求方法", type = "String")
  private String method;

  @Column(length = 10)
  @Schema(title = "请求方式", type = "String")
  private String requestMethod;

  @Column(length = 12)
  @Schema(title = "操作系统类别", type = "String")
  private String operatorType;

  @Column(length = 64)
  @Schema(title = "操作人员", type = "String")
  @Search(SELECT = SearchEnum.LIKE)
  private String operName;

  @Column
  @Schema(title = "浏览器", type = "String")
  private String browser;

  @Column
  @Schema(title = "请求地址", type = "String")
  private String operUrl;

  @Column(length = 64)
  @Schema(title = "BeanName", type = "String")
  private String beanName;


  @Column(length = 64)
  @Schema(title = "请求IP地址", type = "String")
  private String operIp;

  @Column
  @Schema(title = "操作地点", type = "String")
  private String operLocation;

  @Column(columnDefinition = "longtext")
  @Schema(title = "请求参数", type = "String")
  private String operParam;

  @Column(columnDefinition = "longtext")
  @Schema(title = "返回参数", type = "String")
  private String jsonResult;

  @Column(columnDefinition = "longtext")
  @Schema(title = "错误消息", type = "String")
  private String errorMsg;

  @Column
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Schema(title = "操作时间", type = "Date")
  private Date operTime;

  @Column(length = 20)
  @Schema(title = "消耗时间（毫秒）", type = "Long")
  private Long costTime;

  @Column(length = 6)
  @Schema(title = "异常代码", type = "String")
  private String errCode;

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
