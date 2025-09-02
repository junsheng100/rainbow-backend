package com.rainbow.template.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.Keyword;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.Search;
import com.rainbow.base.annotation.SearchFilter;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.SearchEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Entity
@Table(name = "template_config")
@org.hibernate.annotations.Table(appliesTo = "template_config", comment = "模板配置")
@SearchFilter(@Keyword(key = "status", value = "0"))
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"lcd", "lcu"})
public class TemplateConfig extends BaseEntity {

  @Id
  @Column(nullable = false, length = 20)
  @Schema(title = "主键", type = "String")
  private String id;

  @Schema(title = "模板名称", type = "String")
  @NotBlank(message = "模板名称不能为空")
  @Search(SELECT =SearchEnum.LIKE)
  @Column(columnDefinition = "varchar(64) comment '模板名称' ")
  private String name;

  @Schema(title = "模板说明", type = "String")
  @Search(SELECT =SearchEnum.LIKE)
  @Column(columnDefinition = "varchar(255) comment '模板说明' ")
  private String description;

  @OrderBy
  @Schema(title = "排序号", type = "Integer")
  @Column(columnDefinition = "int(11) comment '排序号' ")
  private Integer orderNum;

  @Schema(title = "模板内容,BASE64", type = "String")
  @NotBlank(message = "模板内容不能为空")
  @Column(columnDefinition = "longtext comment '模板内容,BASE64编码' ")
  private String content;

  @Schema(title = "文件后缀名称", type = "String")
  @Column(columnDefinition = "varchar(32) comment '文件后缀名称' ")
  private String suffix;

  @Schema(title = "是否实体类", type = "Boolean")
  @Column
  private Boolean isEntity;



}
