package com.rainbow.system.enums;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum PushTypeEnums {


  ALL("0", "全部"),
  INCLUDE("1", "本部门及以下"),
  SELF("2", "本部门"),
  ;

  private String code;

  private String message;


  PushTypeEnums(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static PushTypeEnums getByCode(String code) {
    if (code == null) {
      return null;
    }
    for (PushTypeEnums PushTypeEnums : PushTypeEnums.values()) {
      if (PushTypeEnums.getCode().equals(code)) {
        return PushTypeEnums;
      }
    }
    return null;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public static List<Map<String, Object>> toList() {
    List<Map<String, Object>> mapList = new ArrayList<>();
    for (PushTypeEnums type : PushTypeEnums.values()) {
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
    for (PushTypeEnums type : PushTypeEnums.values()) {
      mp.put(type.getCode(), type.getMessage());
    }
    return mp;
  }
}
