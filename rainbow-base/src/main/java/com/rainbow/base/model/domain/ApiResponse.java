package com.rainbow.base.model.domain;

import com.rainbow.base.constant.DataConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录响应")
public class ApiResponse extends Account {

    @Schema(description = "访问令牌")
    private String accessToken;
    
    @Schema(description = "刷新令牌")
    private String refreshToken;
    
    @Schema(description = "令牌类型")
    private String tokenType = DataConstant.JWT_HEADER.trim();
    
    @Schema(description = "过期时间（秒）")
    private Long expiresIn;
    


} 