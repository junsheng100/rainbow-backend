package com.rainbow.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDeptInfo implements Serializable {


  @Schema(title = "用户ID", type = "String")
  private String userId;

  @Schema(title = "用户名", type = "String")
  private String userName;

  @Schema(title = "昵称", type = "String")
  private String nickname;

  @Schema(title = "部门ID", type = "Long")
  private Long deptId;

  @Schema(title = "部门名称", type = "String")
  private String deptName;

}
