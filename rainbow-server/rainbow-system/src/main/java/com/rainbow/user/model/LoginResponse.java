package com.rainbow.user.model;

import com.rainbow.base.constant.DataConstant;
import com.rainbow.base.model.domain.LoginUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录响应")
public class LoginResponse extends LoginUser {

    @Schema(description = "访问令牌")
    private String accessToken;
    
    @Schema(description = "刷新令牌")
    private String refreshToken;
    
    @Schema(description = "令牌类型")
    private String tokenType = DataConstant.JWT_HEADER.trim();
    
    @Schema(description = "过期续签时间（秒）")
    private Long expiresIn;
    
    @Schema(description = "过期重新登录时间（秒）")
    private Long expiresOut;



} 