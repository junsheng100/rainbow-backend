package com.rainbow.system.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(title = "提示信息")
public class TooltipsDto {
    
    @Schema(title = "加载提示")
    private String loading;
    
    @Schema(title = "上一个图像")
    private String previous;
    
    @Schema(title = "下一个图像")
    private String next;
    
    @Schema(title = "不能下载提示")
    private String walle;
    
    @Schema(title = "下载提示")
    private String walls;
} 