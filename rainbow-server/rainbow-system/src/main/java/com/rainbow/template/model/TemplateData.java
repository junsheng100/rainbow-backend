package com.rainbow.template.model;

import com.rainbow.template.entity.TemplateField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TemplateData implements Serializable {


  @Schema(title = "类名称", type = "String")
  private String entityName;


  @Schema(title = "数据库分组目录", type = "String")
  private String dbCatalog;

  @Schema(title = "数据库名|命名空间", type = "String")
  private String dbSchema;


  @Schema(title = "表名称", type = "String")
  private String tableName;

  @Schema(title = "作者名称", type = "String")
  private String author;


  @Schema(title = "请求地址", type = "String")
  private String requestUrl;


  @Schema(title = "表描述说明", type = "String")
  private String entityComment;

  @Schema(title = "包路径", type = "String")
  private String packageName;

  @Schema(title = "排序号", type = "Integer")
  private Integer orderNum;

  private List<TemplateField> fieldList;

}
