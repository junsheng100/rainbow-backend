package com.rainbow.template.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.Search;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.SearchEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "template_entity")
@org.hibernate.annotations.Table(appliesTo = "template_entity", comment = "模板-实体类")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcu","lcd","lcu"})
public class TemplateEntity extends BaseEntity {

  @Id
  @Column(length = 20)
  private String id;

  @UnionKey
  @Schema(title = "类名称", type = "String")
  @NotBlank(message = "实体类名 不能为空")
  @Search(SELECT = SearchEnum.LIKE)
  @Column(columnDefinition = "varchar(64) comment '实体类名' ")
  private String entityName;

  @Schema(title = "表名称", type = "String")
  @Column(columnDefinition = "varchar(64) comment '表名称' ")
  private String tableName;

  @Schema(title = "包路径", type = "String")
  @NotBlank(message = "生成包路径不能为空")
  @Column(columnDefinition = "varchar(64) comment '包路径' ")
  private String packageName;

  @Schema(title = "作者名称", type = "String")
  @Column(columnDefinition = "varchar(64) comment '表名称' ")
  private String author;

  @Schema(title = "ID类型", type = "String")
  @Column(columnDefinition = "varchar(32) comment 'ID类型' ")
  private String idType;

  @Transient
  @Schema(title = "ID类型短名称", type = "String")
  private String idShortType;


//  @Schema(title = "所属项目", type = "String")
//  @Column(columnDefinition = "varchar(64) comment '所属项目' ")
//  private String project;

//  @Schema(title = "所属模块", type = "String")
//  @Column(columnDefinition = "varchar(64) comment '所属模块' ")
//  private String module;

  @Schema(title = "数据库类别", type = "String")
  @Column(columnDefinition = "varchar(32) comment '数据库类别' ")
  private String dbType;

  @Schema(title = "数据库分组目录", type = "String")
  @Column(columnDefinition = "varchar(64) comment '数据库分组目录' ")
  private String dbCatalog;

  @Schema(title = "数据库名|命名空间", type = "String")
  @Column(columnDefinition = "varchar(64) comment '数据库|命名空间' ")
  private String dbSchema;


  @Schema(title = "表描述说明", type = "String")
  @Column(columnDefinition = "varchar(64) comment '表描述说明' ")
  @NotBlank(message = "表描述不能为空")
  private String entityComment;


  @OrderBy(value = Sort.Direction.ASC,INDEX = "0")
  @Schema(title = "排序号", type = "Integer")
  @Column(columnDefinition = "varchar(11) comment '排序号' ")
  private Integer orderNum;

//  @Schema(title = "请求地址", type = "String")
//  @Column(columnDefinition = "varchar(64) comment '请求地址' ")
//  private String requestUrl;


  @Transient
  @Schema(title = "字段列表", type = "List")
  private List<TemplateField> fieldList;


//  public String getIdShortType() {
//    if(StringUtils.isNotBlank(idType)){
//      idShortType = idType.substring(idType.lastIndexOf(".") + 1);
//    }
//    return idShortType;
//  }
}
