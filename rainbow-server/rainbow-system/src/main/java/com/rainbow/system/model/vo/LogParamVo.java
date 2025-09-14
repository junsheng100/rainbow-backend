package com.rainbow.system.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rainbow.base.annotation.Search;
import com.rainbow.base.enums.SearchEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.system.model.vo
 * @Filename：LogVo
 * @Date：2025/9/10 16:27
 * @Describe:
 */
@Data
public class LogParamVo implements Serializable {

  @Schema(title = "关键词", type = "String")
  private String keyword;

  @Search(SELECT = SearchEnum.GREATER_EQ, COLUMN = "operTime")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Schema(title = "开始时间", type = "Date")
  private Date startTime;

  @Search(SELECT = SearchEnum.LESS_EQ, COLUMN = "operTime")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Schema(title = "结束时间", type = "Date")
  private Date endTime;


}
