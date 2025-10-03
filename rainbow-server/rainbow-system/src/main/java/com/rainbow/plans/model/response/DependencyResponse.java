package com.rainbow.plans.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.plans.enums.DependencyType;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.plans.model.response
 * @Filename：DependencyReponse
 * @Date：2025/9/28 12:36
 * @Describe:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DependencyResponse implements Serializable {

  private String planId;
  private String name;
  private String prevTaskId;
  private String prevTaskName;
  private String postTaskId;
  private String postTaskName;
  private Integer sequenceOrder;
  private Boolean isCriticalPath;
  private DependencyType dependencyType;


  public DependencyResponse() {
  }

  public DependencyResponse(String planId, String prevTaskId, String postTaskId, Boolean isCriticalPath) {
    this.planId = planId;
    this.prevTaskId = prevTaskId;
    this.postTaskId = postTaskId;
    this.isCriticalPath = isCriticalPath;
  }

}
