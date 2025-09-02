package com.rainbow.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Schema(title = "壁纸查询请求")
public class WallpaperQueryRequest {
    
    @Schema(title = "开始日期(YYYYMMDD)", example = "20241201")
    private String startDate;
    
    @Schema(title = "结束日期(YYYYMMDD)", example = "20241231")
    private String endDate;
    
    @Schema(title = "是否精选")
    private Boolean isFeatured;
    
    @Schema(title = "下载状态(0:未下载,1:已下载,2:下载失败)")
    private Integer downloadStatus;
    
    @Schema(title = "页码", example = "1")
    @Min(value = 1, message = "页码不能小于1")
    private Integer page = 1;
    
    @Schema(title = "每页大小", example = "10")
    @Min(value = 1, message = "每页大小不能小于1")
    @Max(value = 100, message = "每页大小不能大于100")
    private Integer size = 10;
    
    @Schema(title = "排序字段", example = "startDate")
    private String sortBy = "startDate";
    
    @Schema(title = "排序方向(asc/desc)", example = "desc")
    private String sortDir = "desc";
} 