package com.rainbow.template.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.ExcelCell;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "template_data_type")
@org.hibernate.annotations.Table(appliesTo = "template_data_type", comment = "数据类型")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcu","lcd","lcu"})
public class TemplateDataType extends BaseEntity {

  @Id
  @Schema(title = "主键",type = "String")
  @Column(length = 20)
  private String id;

  @UnionKey
  @ExcelCell(value = 0,size = 3,title = "数据库名称")
  @Schema(title = "数据库名称", type = "String")
  @Column(length = 32)
  private String dbType;

  @ExcelCell(value = 1,size = 3,title = "数据类型")
  @Schema(title = "数据类型", type = "String")
  @Column(length = 32)
  private String dataType;

  @UnionKey
  @ExcelCell(value = 2,size = 5,title = "类型值")
  @Schema(title = "类型值", type = "String")
  @Column(length = 32)
  private String columnType;


  @ExcelCell(value = 3,size = 8,title = "数据范围")
  @Schema(title = "大小/范围描述", type = "String")
  @Column(length = 64)
  private String dataRange;

  @ExcelCell(value = 4,size = 8,title = "描述")
  @Schema(title = "说明", type = "String")
  @Column(length = 64)
  private String dataDescribe;

  @ExcelCell(value =5,size = 8,title = "Java 类型")
  @Schema(title = "Java 类型", type = "String")
  @Column(length = 50)
  private String javaType;

  @OrderBy
  @ExcelCell(value =6,size = 1,title = "排序号")
  @Schema(title = "排序号", type = "Integer")
  @Column(columnDefinition = "int(11) comment '排序号' ")
  private Integer orderNum;


  @Schema(title = "更新时间",type = "LocalDateTime")
  @Column
  @ExcelCell(value =7,size = 1,title = "更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updateTime;

}
