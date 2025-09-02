package com.rainbow.system.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(title = "壁纸响应数据")
public class WallpaperResponse {
    
    @Schema(title = "壁纸ID")
    private Long id;
    
    @Schema(title = "开始日期")
    private String startDate;
    
    @Schema(title = "标题")
    private String title;
    
    @Schema(title = "版权信息")
    private String copyright;
    
    @Schema(title = "版权链接")
    private String copyrightLink;
    
    @Schema(title = "预览图URL")
    private String previewUrl;
    
    @Schema(title = "下载URL")
    private String downloadUrl;
    
    @Schema(title = "完整图片URL")
    private String fullUrl;
    
    @Schema(title = "本地存储路径")
    private String localPath;
    
    @Schema(title = "文件大小(字节)")
    private Long fileSize;
    
    @Schema(title = "查看次数")
    private Integer viewCount;
    
    @Schema(title = "下载次数")
    private Integer downloadCount;
    
    @Schema(title = "是否精选")
    private Boolean isFeatured;
    
    @Schema(title = "下载状态(0:未下载,1:已下载,2:下载失败)")
    private Integer downloadStatus;
    
    @Schema(title = "是否可用作壁纸")
    private Boolean wp;
    
    @Schema(title = "哈希值")
    private String hsh;
    
    @Schema(title = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    
    @Schema(title = "下载时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime downloadTime;
} 