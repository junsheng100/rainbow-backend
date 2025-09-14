package com.rainbow.files.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.system.model.dto
 * @Filename：ResourceConfig
 * @Date：2025/9/10 19:20
 * @Describe:
 */
@Data
public class ResourceConfig implements Serializable {

  @NotBlank(message = "预览 URL 不能为空")
  private String previewUrl;

  @NotBlank(message = "文件上 URL 不能为空")
  private String uploadUrl;

  @NotBlank(message = "文件 最大尺寸 不能为空")
  @Size(min = 1, message = "文件 允许最大尺寸 不能小于 1")
  private Long maxSize;


  @NotBlank(message = "许可文件扩展名 不能为空")
  private String[] allowExtensions;

  @NotBlank(message = "预览文件扩展名 不能为空")
  private String[] previewType;


//  @NotBlank(message = "文件扩展名 不能为空")
  private String fileExt;

  @Schema(title = "是否可预览")
  private Boolean hasPreview;

}
