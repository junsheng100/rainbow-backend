package com.rainbow.system.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.system.model.vo
 * @Filename：OperLogData
 * @Describe:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OperLogData implements Serializable {

  private String beanName;
  private String method;
  private String operTime;

  private String title;

  private Long total;


  public OperLogData() {
  }

  public OperLogData(String beanName, String method,String title, Long count) {
    this.beanName = beanName;
    this.method = method;
    this.title = title;
    this.total = count;
  }



  public OperLogData(String title,String beanName, String method,String operTime, Long count) {
    this.title = title;
    this.beanName = beanName;
    this.method = method;
    this.operTime = operTime;
    this.total = count;
  }
}
