package com.rainbow.base.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class OperLogVo implements Serializable {

  @Schema(title = "请求",type = "String")
  private String title;

  @Schema(title = "请求方法",type = "String")
  private String method;

  @Schema(title = "请求方式",type = "String")
  private String requestMethod;

  @Schema(title = "操作系统类别",type = "String")
  private String operatorType;

  @Schema(title = "操作人员",type = "String")
  private String operName;


  @Schema(title = "浏览器",type = "String")
  private String browser;

  @Column(length = 64)
  private String beanName;


  @Schema(title = "请求地址",type = "String")
  private String operUrl;

  @Schema(title = "请求IP地址",type = "String")
  private String operIp;

  @Schema(title = "操作地点",type = "String")
  private String operLocation;

  @Schema(title = "请求参数",type = "String")
  private String operParam;

  @Schema(title = "返回参数",type = "String")
  private String jsonResult;

  @Schema(title = "错误消息",type = "String")
  private String errorMsg;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Schema(title = "操作时间",type = "Date")
  private Date operTime;

  @Schema(title = "消耗时间（毫秒）",type = "Long")
  private Long costTime;

  @Schema(title = "异常代码",type = "String")
  private String errCode;
}
