package com.rainbow.files.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Sort;

import javax.persistence.Column;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileTypeModel extends BaseEntity {


  @Schema(title = "分组名称",type = "String")
  private String typeName;

  @Column(length = 32)
  @OrderBy(value = Sort.Direction.ASC,INDEX = "2")
  @Schema(title = "分组名称",type = "String")
  private String typeGroup;


  @Schema(title = "扩展名称",type = "String")
  private String extension;

  @Schema(title = "mime-type 类型",type = "String")
  private String mimeType;







}
