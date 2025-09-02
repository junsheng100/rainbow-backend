package com.rainbow.template.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "template_result")
@org.hibernate.annotations.Table(appliesTo = "template_result", comment = "模板数据")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateResult extends BaseEntity {

  @Id
  @Column(length = 20)
  private String id;

  @Schema(title = "文件名称", type = "String")
  @Column(length = 64)
  private String fileName;

  @UnionKey
  @Schema(title = "实体类id", type = "String")
  @Column(length = 32)
  private String entityId;

  @Column(length = 64)
  @Schema(title = "包路径", type = "String")
  @NotBlank(message = "生成包路径不能为空")
  private String packageName;

  @UnionKey
  @Schema(title = "配置类 id", type = "String")
  @Column(length = 32)
  private String configId;

  @Transient
  @Schema(title = "配置名称", type = "String")
  private String configName;


  @Schema(title = "模板内容", type = "String")
  @Column(columnDefinition = "longtext comment '模板内容' ")
  private String content;

  @Schema(title = "来源内容", type = "String")
  @Column(columnDefinition = "longtext comment '来源内容' ")
  private String srcContent;


  @Schema(title = "来源内容", type = "String")
  @Column(length = 255)
  @Size(min = 0,max = 255)
  private String description;


}
