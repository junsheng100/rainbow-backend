package com.rainbow.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.system.enums.PushTimeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_notice_push")
@org.hibernate.annotations.Table(appliesTo = "sys_notice_push", comment = "通知公告推送")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd", "fcu", "lcd", "lcu"})
public class SysNoticePush extends BaseEntity {


  @Id
  @Schema(title = "主键", type = "Long")
  @Column(length = 20)
  private String pushId;

  @Column(length = 20)
  @Schema(title = " 公告键", type = "Long")
  private Long noticeId;

  @Transient
  @Schema(title = "公告标题", type = "String")
  private String noticeTitle;


  @Column(length = 20)
  @Schema(title = "部门ID", type = "Long")
  private Long deptId;

  @Transient
  @Schema(title = "部门名称", type = "String")
  private String deptName;


  @Column(length = 36)
  @Schema(title = "用户ID", type = "String")
  private String userId;

  @Transient
  @Schema(title = "用户ID", type = "String")
  private String nickname;

  @OrderBy(value = Sort.Direction.DESC, INDEX = "0")
  @Schema(title = "推送时间", type = "LocalDateTime")
  @Column
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime pushTime;

  @Schema(title = "推送状态 0/1 :待推送/已推送", type = "Integer")
  @Column(length = 1)
  private Integer isPush;


  @Schema(title = "推送时间选项 '1:immediate' , '2:delayed'", type = "PushTimeType")
  @Column(length = 1)
  @Enumerated(EnumType.ORDINAL)
  private PushTimeType pushTimeType;


  @Schema(title = "是否已读 0/1 :否/是", type = "Integer")
  @Column(length = 1)
  private Integer isRead;


  @Schema(title = "阅读次数", type = "Integer")
  @Column(columnDefinition = "int(11) default 0 ")
  private Integer readCount;


  @Schema(title = "推送方式 0/1/2 :全部/本部门及以下/本部门", type = "Integer")
  @Column(length = 1)
  private Integer pushType;


}
