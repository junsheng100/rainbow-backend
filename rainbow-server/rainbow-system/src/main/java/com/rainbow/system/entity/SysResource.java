package com.rainbow.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.Search;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.enums.SearchEnum;
import com.rainbow.base.utils.Md5Utils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
import java.math.BigInteger;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_resource")
@org.hibernate.annotations.Table(appliesTo = "sys_resource", comment = "系统资源")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu"})
public class SysResource extends BaseEntity {

  @Id
  @Schema(title = "主键",type = "Long")
  @Column(length = 20)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Search(SELECT = SearchEnum.LIKE)
  @Schema(title = "资源名称",type = "String")
  @Column(length = 256)
  private String originalFilename;

  @Schema(title = "URL路径",type = "String")
  @Column(length = 256)
  private String fileUrl;


  @Schema(title = "资源大小",type = "BigInteger")
  @Column(length = 32)
  private BigInteger size;

  @Search(SELECT = SearchEnum.LIKE)
  @Schema(title = "资源类型",type = "String")
  @Column
  private String contentType;

  @Schema(title = "后缀",type = "String")
  @Column(length = 12)
  private String fileExt;

  @Schema(title = "资源分组",type = "String")
  @Column(length = 12)
  private String fileGroup;

  @UnionKey
  @Schema(title = "MD5码",type = "String")
  @Column(length = 36)
  private String md5Code;

  @Schema(title = "数据来源ID",type = "String")
  @Column(length = 36)
  private String srcId;

  @Transient
  @Schema(title = "标志",type = "String")
  private String logo;

  public SysResource() {

  }

  public SysResource(MultipartFile multipartFile) {
    this.originalFilename = multipartFile.getOriginalFilename();
    this.size = BigInteger.valueOf(multipartFile.getSize());
    this.contentType = multipartFile.getContentType();
    this.fileGroup = multipartFile.getContentType().split(ChartEnum.SLASH.getCode())[0];
  }

  public SysResource(File file) {
    this.originalFilename = file.getName();
    this.size = BigInteger.valueOf(file.length());
    this.contentType =  originalFilename.substring(originalFilename.lastIndexOf(".")+1);
    this.md5Code = Md5Utils.getMD5(file);
    this.fileGroup = contentType;
    this.fileExt = contentType;
  }



}
