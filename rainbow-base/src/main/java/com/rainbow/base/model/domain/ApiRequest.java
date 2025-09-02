package com.rainbow.base.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Null;

@Data
@Schema(description = "API请求")
public class ApiRequest {

    @Null(message = "API-KEY不能为空")
    @Schema(title = "API密钥", type = "String")
    private String apiKey;

    @Null(message = "密钥不能为空")
    @Schema(title = "密钥", type = "String")
    private String secretKey;

} 