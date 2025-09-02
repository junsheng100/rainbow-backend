package com.rainbow.base.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

  private int code;
  private String message;
  private long timestamp;
  private List<String> details; // 用于存储验证错误等详细信息

  public ErrorResponse(int code, String message, long timestamp) {
    this.code = code;
    this.message = message;
    this.timestamp = timestamp;
  }

}
