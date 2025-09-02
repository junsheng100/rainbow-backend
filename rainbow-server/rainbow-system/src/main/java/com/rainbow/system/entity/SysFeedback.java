package com.rainbow.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.Search;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.SearchEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@Entity
@Table(name = "sys_feedback")
@org.hibernate.annotations.Table(appliesTo = "sys_feedback", comment = "意见反馈")
@JsonIgnoreProperties({"fcu","lcd","lcu"})
public class SysFeedback extends BaseEntity {


  @Id
  @Schema(title = "主键", type = "String")
  @Column(length = 20)
  private String id;

  @Schema(title = "用户ID", type = "String")
  @Column(length = 20)
  private String feedbackBy;

  @Transient
  @Schema(title = "用户名", type = "String")
  private String userName;


  @Schema(title = "标题", type = "String")
  @Search(SELECT = SearchEnum.LIKE)
  @Column(columnDefinition = "varchar(200) default null comment '标题' ")
  @Size(min = 0, max = 200, message = "标题长度不能超过200个字符")
  private String title;


  @Schema(title = "反馈内容", type = "String")
  @Column(columnDefinition = "longtext default null comment '反馈内容' ")
  @NotBlank(message = "反馈内容不能为空")
  private String content;


  @Schema(title = "回复内容", type = "String")
  @Column(columnDefinition = "varchar(2000) default null comment '回复内容' ")
  @Size(min = 0, max = 2000, message = "回复内容长度不能超过2000个字符")
  private String reply;


  @Schema(title = "回复用户ID", type = "String")
  @Column(length = 20)
  private String replyBy;

  @OrderBy(value = Sort.Direction.DESC, INDEX = "1")
  @Schema(title = "回复时间", type = "LocalDateTime")
  @Column(columnDefinition = "datetime default null comment '回复时间' ")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime replyTime;

  @Column(columnDefinition = "int default 0 comment '处理阶段（0:待处理,1:处理中,2:已完成）' ")
  @Schema(title = "处理阶段（0:待处理,1:处理中,2:已完成）", type = "Integer")
  public Integer stage;

  @Transient
  @Schema(title = "开始时间", type = "Date")
  @Search(SELECT = SearchEnum.GREATER_EQ, COLUMN = "fcd")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date startTime;

  @Transient
  @Schema(title = "结束时间", type = "Date")
  @Search(SELECT = SearchEnum.LESS_EQ, COLUMN = "fcd")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date endTime;



}
