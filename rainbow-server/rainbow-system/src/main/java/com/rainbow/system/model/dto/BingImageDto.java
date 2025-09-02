package com.rainbow.system.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(title = "Bing图片数据")
public class BingImageDto {
    
    @Schema(title = "开始日期")
    private String startdate;
    
    @Schema(title = "完整开始日期时间")
    private String fullstartdate;
    
    @Schema(title = "结束日期")
    private String enddate;
    
    @Schema(title = "图片URL")
    private String url;
    
    @Schema(title = "图片基础URL")
    private String urlbase;
    
    @Schema(title = "版权信息")
    private String copyright;
    
    @Schema(title = "版权链接")
    private String copyrightlink;
    
    @Schema(title = "标题")
    private String title;
    
    @Schema(title = "测验链接")
    private String quiz;
    
    @Schema(title = "是否可用作壁纸")
    private Boolean wp;
    
    @Schema(title = "哈希值")
    private String hsh;
    
    @Schema(title = "深色模式")
    private Integer drk;
    
    @Schema(title = "顶部标记")
    private Integer top;
    
    @Schema(title = "底部标记")
    private Integer bot;
    
    @Schema(title = "热点区域")
    private List<String> hs;
} 