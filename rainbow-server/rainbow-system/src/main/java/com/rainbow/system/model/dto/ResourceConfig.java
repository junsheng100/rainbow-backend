package com.rainbow.system.model.dto;

import lombok.Data;

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


  private String previewUrl;

  private String uploadUrl;

  private Long maxSize;

  private String[] allowExtensions;

}
