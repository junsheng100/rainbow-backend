package com.rainbow.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.Search;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.SearchEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/***
 * 岗位（Position）
 * 指组织架构中一个固定的、正式的工作位置，有明确的职责边界和上下级关系。
 * 例如：销售部经理、财务分析师、人力资源专员。
 * 特点是静态的，通常通过《岗位说明书》明确描述职责、权限和任职要求。
 */
@Getter
@Setter
@Entity
@Table(name = "post_info")
@org.hibernate.annotations.Table(appliesTo = "post_info", comment = "职位")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu"})
public class PostInfo extends BaseEntity {

  /**
   *
   */
  @Id
  @Column(length = 20)
  @Schema(title = "岗位序号",type = "Long")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long postId;

  @UnionKey
  @Column(length = 64)
  @Schema(title = "岗位编码",type = "String")
  @NotBlank(message = "岗位编码不能为空")
  @Size(min = 0, max = 64, message = "岗位编码长度不能超过64个字符")
  private String postCode;


  @Column(length = 64)
  @Schema(title = "岗位名称",type = "String")
  @Search(SELECT = SearchEnum.LIKE)
  @NotBlank(message = "岗位名称不能为空")
  @Size(min = 0, max = 50, message = "岗位名称长度不能超过50个字符")
  private String postName;


  @Column
  @Schema(title = "简介",type = "String")
  private String overview;


  @Column(length = 11)
  @Schema(title = "岗位排序",type = "String")
  @NotNull(message = "显示顺序不能为空")
  @OrderBy(value = Sort.Direction.ASC,INDEX = "10")
  private Integer orderNum;



}
