package com.rainbow.system.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.system.model.vo
 * @Filename：OperLogMonthData
 * @Describe:
 */
@Data
public class OperLogMonthData implements Serializable {

  private String time;

  private List<OperLogData> dataList;

  public OperLogMonthData() {
  }


  public OperLogMonthData(String time, List<OperLogData> dataList) {
    this.time = time;
    this.dataList = dataList;
  }

}
