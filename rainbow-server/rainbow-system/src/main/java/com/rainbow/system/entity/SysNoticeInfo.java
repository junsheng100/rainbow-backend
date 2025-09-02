package com.rainbow.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.Search;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.InfoStage;
import com.rainbow.base.enums.SearchEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_notice_info")
@org.hibernate.annotations.Table(appliesTo = "sys_notice_info", comment = "通知公告表")
@JsonIgnoreProperties({"fcu","lcd","lcu"})
public class SysNoticeInfo extends BaseEntity {

  @Id
  @Column(length = 20)
  @Schema(title = "主键", type = "Long")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long noticeId;


  @Column(length = 128)
  @Search(SELECT = SearchEnum.LIKE)
  @Schema(title = "公告标题", type = "String")
  private String noticeTitle;


  @Column(length = 1)
  @Schema(title = " 公告类型（1通知 2公告）", type = "String")
  private String noticeType;


  @Column(columnDefinition = "longblob")
  @Schema(title = " 公告内容", type = "String")
  private String noticeContent;

  @Column(columnDefinition = "varchar(12)")
  @Schema(title = " 发文阶段", type = "InfoStage")
  private InfoStage stage;

  @Transient
  @Schema(title = " 推送统计【部门】", type = "Integer")
  private Integer deptCount;

  @Transient
  @Schema(title = " 推送统计【人员】", type = "Integer")
  private Integer userCount;

  @Transient
  @Schema(title = " 已读【人员】", type = "Integer")
  private Integer readCount;

  @Schema(title = "推送方式 0/1/2 :全部/本部门及以下/本部门", type = "Integer")
  @Column(length = 1)
  private Integer pushType;

  @OrderBy(value = Sort.Direction.DESC, INDEX = "0")
  @Schema(title = "推送时间", type = "LocalDateTime")
  @Column
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime pushTime;


  @Column(columnDefinition = "longtext")
  @Schema(title = "推送部门", type = "String")
  private String deptIds;

  @Transient
  @Schema(title = "推送部门", type = "String")
  private String deptNames;


  @Column(columnDefinition = "longtext")
  @Schema(title = " 推送人员", type = "String")
  @Search(SELECT = SearchEnum.LIKE)
  private String userIds;

  @Transient
  @Schema(title = " 推送人员", type = "String")
  private String userNames;






}
