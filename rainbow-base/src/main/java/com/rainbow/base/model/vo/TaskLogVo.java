package com.rainbow.base.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.base.model.vo
 * @Filename：TaskLogVo
 * @Describe:
 */
@Data
public class TaskLogVo implements Serializable {

  @Schema(title = "任务ID", type = "String")
  private String taskId;


  @Schema(title = "任务名", type = "String")
  private String taskName;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Schema(title = "执行时间", type = "Date")
  private Date execTime;

  @Schema(title = "执行结果(0:成功 1:失败)", type = "Integer")
  private Integer execResult;

  @Schema(title = "错误信息", type = "String")
  private String errorMessage;

  @Schema(title = "执行时长(毫秒)", type = "Long")
  private Long execDuration;

  private Date startTime;

  private Date endTime;


  private Long getExecDuration() {
    if (startTime != null && endTime != null) {
      return endTime.getTime() - startTime.getTime();
    }
    return 0L;
  }

}
