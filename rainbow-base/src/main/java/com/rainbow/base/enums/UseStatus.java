package com.rainbow.base.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum UseStatus {

  DEL("-1", -1,"已删除"),
  NO("0", 0,"有效"),
  YES("1", 1,"禁用");

  private String code;
  private Integer data;


  private String message;


  UseStatus(String code,Integer data, String message) {
    this.code = code;
    this.data = data;
    this.message = message;
  }

  public static UseStatus getByCode(String code) {
    if (code == null) {
      return null;
    }
    for (UseStatus UseStatus : UseStatus.values()) {
      if (UseStatus.getCode().equals(code)) {
        return UseStatus;
      }
    }
    return null;
  }

  public static UseStatus getByData(Integer data) {
    if (data == null) {
      return null;
    }
    for (UseStatus UseStatus : UseStatus.values()) {
      if (UseStatus.getData().equals(data)) {
        return UseStatus;
      }
    }
    return null;
  }

  public String getCode() {
    return code;
  }

  public Integer getData() {
    return data;
  }

  public static List<Map<String, Object>> toList() {
    List<Map<String, Object>> mapList = new ArrayList<>();
    for (UseStatus type : UseStatus.values()) {
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
    for (UseStatus type : UseStatus.values()) {
      mp.put(type.getCode(), type.getMessage());
    }
    return mp;
  }
  
}
