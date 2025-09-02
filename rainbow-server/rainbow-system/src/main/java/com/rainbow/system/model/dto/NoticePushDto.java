package com.rainbow.system.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rainbow.system.enums.PushTimeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class NoticePushDto implements Serializable {


  @Schema(title = "主键", type = "Long")
  private Long noticeId;

  @Schema(title = "推送方式 0/1/2 :全部/本部门及以下/本部门", type = "Integer")
  private Integer pushType;


  @Schema(title = "推送时间选项 '1:immediate' , '2:delayed'", type = "PushTimeType")
  @Enumerated(EnumType.ORDINAL)
  private PushTimeType pushTimeType;

  @Schema(title = "推送时间", type = "LocalDateTime")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime pushTime;

  @Transient
  @Schema(title = " 推送部门", type = "Long")
  private List<Long> deptIds;


  @Transient
  @Schema(title = " 推送人员", type = "String")
  private List<String> userIds;


  @Schema(title = " 推送统计【部门】", type = "Integer")
  private Integer deptCount;


  @Schema(title = " 推送统计【人员】", type = "Integer")
  private Integer userCount;


  @Schema(title = " 已读【人员】", type = "Integer")
  private Integer readCount;


  public NoticePushDto() {
  }


  public NoticePushDto(Long noticeId,LocalDateTime pushTime,List<Long> deptIds, List<String> userIds) {
    this.noticeId = noticeId;
    this.pushTime = pushTime;
    this.deptIds = deptIds.stream().distinct().collect(Collectors.toList());
    this.userIds = userIds.stream().distinct().collect(Collectors.toList());
    this.deptCount = CollectionUtils.isEmpty(deptIds) ? 0 : deptIds.size();
    this.userCount = CollectionUtils.isEmpty(userIds) ? 0 : userIds.size();
  }

}
