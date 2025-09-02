package com.rainbow.system.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(title = "Bing API响应数据")
public class BingApiResponse {
    
    @Schema(title = "壁纸图片列表")
    private List<BingImageDto> images;
    
    @Schema(title = "提示信息")
    private TooltipsDto tooltips;
} 