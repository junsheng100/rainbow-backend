package com.rainbow.appdoc.enums;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum DataTypeEnums {


  RETURN("0", "出参"),
  PARAMS("1", "入参"),
  ;

  private String code;

  private String message;


  DataTypeEnums(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static DataTypeEnums getByCode(String code) {
    if (code == null) {
      return null;
    }
    for (DataTypeEnums PushTypeEnums : DataTypeEnums.values()) {
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
    for (DataTypeEnums type : DataTypeEnums.values()) {
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
    for (DataTypeEnums type : DataTypeEnums.values()) {
      mp.put(type.getCode(), type.getMessage());
    }
    return mp;
  }
}
