package com.rainbow.template.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.Search;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.SearchEnum;
import com.rainbow.base.utils.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "template_field")
@EqualsAndHashCode(callSuper = true)
@org.hibernate.annotations.Table(appliesTo = "template_field", comment = "模板-属性信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcu","lcd","lcu"})
public class TemplateField extends BaseEntity {

  @Id
  @Column(length = 20)
  private String id;

  @NotNull(message = "模板实体ID 不能为空")
  @Schema(title = "模板实体类 id", type = "String")
  @Column(columnDefinition = "varchar(32) comment '模板实体类id'")
  private String entityId;

  @Transient
  @Schema(title = "类名称", type = "String")
  private String entityName;

  @Schema(title = "字段名称", type = "String")
  @NotBlank(message = "字段名称不能为空")
  @Search(SELECT = SearchEnum.LIKE)
  @Column(columnDefinition = "varchar(32) comment '字段名称'")
  private String fieldName;
  
  @Schema(title = "字段类型", type = "String")
  @NotBlank(message = "属性类型不能为空")
  @Column(columnDefinition = "varchar(32) comment '字段类型'")
  private String fieldType;

  @Transient
  @Schema(title = "字段类型", type = "String")
  private String shortType;


  
  @Schema(title = "字段类型", type = "String")
  @NotBlank(message = "字段类型不能为空")
  @Column(columnDefinition = "varchar(32) comment '字段类型'")
  private String columnType;

  @Schema(title = "字段描述", type = "String")
  @Size(min=0,max = 64)
  @Column(columnDefinition = "varchar(64) comment '字段类型'")
  private String fieldComment;
  
  @Schema(title = "字段长度", type = "Integer")
  @NotNull(message = "字段长度不能为空")
  @Column(length = 11)
  private Integer fieldLength;
  
  @Schema(title = "字段精度", type = "Integer")
  @Column(columnDefinition = "int(11) comment '字段精度'")
  private Integer fieldScale;


  @OrderBy(value = Sort.Direction.ASC,INDEX = "0")
  @Schema(title = "排序号", type = "Integer")
  @Column(columnDefinition = "varchar(11) comment '排序号' ")
  private Integer orderNum;

  @Schema(title = "字段是:0,否:1为空", type = "Byte")
  @Column
  private Boolean isNull;

  @Schema(title = "字段是否主键", type = "Byte")
  @Column
  private Boolean isPk;

  @Schema(title = "字段是否自增", type = "Byte")
  @Column
  private Boolean isAuto;


  @Schema(title = "字段是否唯一", type = "Byte")
  @Column
  private Boolean isUnique;

  @Schema(title = "字段是否外关联", type = "String")
  @Column
  private Boolean isRel;


  @Column(columnDefinition = "varchar(32) comment '关联类型'")
  @Schema(title = "关联类型， 数据字典", type = "String")
  private String relType;

  @Schema(title = "字段外关联Entity", type = "String")
  @Column(columnDefinition = "varchar(64) comment '字段外关联Entity'")
  private String relEntity;

  @Column(columnDefinition = "varchar(64) comment '字段外关联属性字段'")
  @Schema(title = "关联属性", type = "String")
  private String relField;

  @NotNull(message = "字段外关联字段不能为空")
  @Schema(title = "字段外关联字段说明", type = "String")
  @Column(columnDefinition = "varchar(64) comment '字段外关联字段说明'")
  private String fieldFkFieldComment;


  public String getShortType() {
    if(StringUtils.isNotBlank(fieldType)){
      shortType = fieldType.substring(fieldType.lastIndexOf(".") + 1);
    }
    return shortType;
  }
}
