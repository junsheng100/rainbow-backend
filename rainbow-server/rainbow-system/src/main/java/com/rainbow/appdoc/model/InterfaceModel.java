package com.rainbow.appdoc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.appdoc.model
 * @Filename：InterfaceModel
 * @Date：2025/8/7 10:20
 * @Describe:
 */
@Data
public class InterfaceModel implements Serializable {

  /**
   * ID
   */
  private String id;
  /**
   * 方法命名
   */
  private String methodName;

  /**
   * 请求URL
   */
  private String requestUrl;

  /**
   * 请求方法（GET、POST等）
   */
  private String requestMethod;

  private String clientTypes;

  private String description;

  private Integer disabled;

  private Integer valid;

  public InterfaceModel() {
  }

  public InterfaceModel(String id, String methodName, String requestUrl,
                        String requestMethod, String description, String clientTypes, Integer disabled,Integer valid) {
    this.id = id;
    this.methodName = methodName;
    this.requestUrl = requestUrl;
    this.requestMethod = requestMethod;
    this.description = description;
    this.clientTypes = clientTypes;
    this.disabled = disabled;
    this.valid = valid;
  }

}
