package com.rainbow.system.entity;

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cfg_file_type")
@org.hibernate.annotations.Table(appliesTo = "cfg_file_type", comment = "文件类型管理")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu"})
public class FileType extends BaseEntity {

  @Id
  @Schema(title = "主键",type = "String")
  @Column(length = 20)
  private String id;

  @Column(length = 64)
  @Search(SELECT = SearchEnum.LIKE)
  @OrderBy(value = Sort.Direction.ASC,INDEX = "3")
  @NotBlank(message = "文件类型名称不能为空")
  @Size(min = 0, max = 64, message = "菜单名称长度不能超过64个字符")
  private String typeName;

  @Column(length = 32)
  @OrderBy(value = Sort.Direction.ASC,INDEX = "2")
  @Schema(title = "分组名称",type = "String")
  private String typeGroup;

  @UnionKey
  @Column(length = 32)
  @Schema(title = "扩展名称",type = "String")
  @Search(SELECT = SearchEnum.LIKE)
  @NotBlank(message = "扩展名称不能为空")
  @Size(min = 0, max = 32, message = "参数名称不能超过32个字符")
  private String extension;

  @UnionKey
  @Column(length = 2000)
  @Schema(title = "mime-type 类型",type = "String")
  @Search(SELECT = SearchEnum.LIKE)
  @NotBlank(message = "mime-type 类型不能为空")
  @Size(min = 0, max = 2000, message = "mime-type 类型长度不能超过255个字符")
  private String mimeType;

  @Column(length = 1)
  @Schema(title = "是否拒绝",type = "Integer")
  private Integer refuse;

  @Column(length = 1)
  @OrderBy(value = Sort.Direction.DESC,INDEX = "1")
  @Schema(title = "是否许可",type = "Integer")
  private Integer approve;

  @Column
  @Schema(title = "归属机构",type = "String")
  private String company;

  @Column
  @Schema(title = "icon图标",type = "String")
  private String icon;

  @Column
  @Schema(title = "logo图标",type = "String")
  private String logo;

  @Column(length = 255)
  @OrderBy(value = Sort.Direction.ASC,INDEX = "4")
  @Schema(title = "备注",type = "String")
  @Size(min = 0, max = 255, message = "参数名称不能超过255个字符")
  private String remark;


  public FileType(){

  }
  public FileType(String extension, String mimeType){
    this.extension = extension;
    this.mimeType = mimeType;
    this.typeGroup = mimeType.substring(0,mimeType.indexOf("/"));
  }



}
