package com.rainbow.base.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class LoginUser implements Serializable {

    @Schema(title = "用户ID",type = "String")
    private String userId;

    @Schema(title = "用户名",type = "String")
    private String userName;

    @Schema(title = "昵称",type = "String")
    private String nickname;

    @Schema(title = "用户类型",type = "String")
    private String userType;

    @Schema(title = "头像", type = "String")
    private String avatar;

    public Date loginTime;

    @Schema(title = "权限",type = "String")
    private List<String> permissions;

    @Schema(title = "角色",type = "String")
    private List<String> roles;


}