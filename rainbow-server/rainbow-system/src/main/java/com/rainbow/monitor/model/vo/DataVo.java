package com.rainbow.monitor.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.monitor.model.vo
 * @Filename：SysDataVo
 * @Describe:
 */
@Data
public class DataVo implements Serializable {


  private String keyword;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date startTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date endTime;

}
