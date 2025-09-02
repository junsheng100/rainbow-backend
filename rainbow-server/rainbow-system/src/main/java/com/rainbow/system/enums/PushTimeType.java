package com.rainbow.system.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum PushTimeType {

  IMMEDIATELY(0,"立即推送"),
  AFTER(1,"定时推送");

  private Integer code;

  private String message;


  PushTimeType(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  public static PushTimeType getByCode(Integer code) {
    if (code == null) {
      return null;
    }
    for (PushTimeType PushTimeType : PushTimeType.values()) {
      if (PushTimeType.getCode().equals(code)) {
        return PushTimeType;
      }
    }
    return null;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public static List<Map<String, Object>> toList() {
    List<Map<String, Object>> mapList = new ArrayList<>();
    for (PushTimeType type : PushTimeType.values()) {
      Map<String, Object> mp = new HashMap<>();
      mp.put("code", type.getCode());
      mp.put("message", type.getMessage());
      mapList.add(mp);
    }
    return mapList;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public static Map toMap() {
    Map mp = new HashMap();
    for (PushTimeType type : PushTimeType.values()) {
      mp.put(type.getCode(), type.getMessage());
    }
    return mp;
  }
}
