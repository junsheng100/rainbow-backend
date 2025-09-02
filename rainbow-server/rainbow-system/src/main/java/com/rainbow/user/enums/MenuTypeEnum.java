package com.rainbow.user.enums;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum MenuTypeEnum {


  CATALOG("C", "目录","Catalog"),
  LEAF("M", "菜单","Menu"),
  FUNCTION("F", "功能","Function");


  private String code;

  private String message;

  private String describe;


  MenuTypeEnum(String code, String message,String describe) {
    this.code = code;
    this.message = message;
    this.describe = describe;
  }

  public static MenuTypeEnum getByCode(String code) {
    if (code == null) {
      return null;
    }
    for (MenuTypeEnum MenuTypeEnum : MenuTypeEnum.values()) {
      if (MenuTypeEnum.getCode().equals(code)) {
        return MenuTypeEnum;
      }
    }
    return null;
  }


  public static List<Map<String, Object>> toList() {
    List<Map<String, Object>> mapList = new ArrayList<>();
    for (MenuTypeEnum type : MenuTypeEnum.values()) {
      Map<String, Object> mp = new HashMap<>();
      mp.put("code", type.getCode());
      mp.put("message", type.getMessage());
      mapList.add(mp);
    }
    return mapList;
  }

  public static Map toMap() {
    Map mp = new HashMap();
    for (MenuTypeEnum type : MenuTypeEnum.values()) {
      mp.put(type.getCode(), type.getMessage());
    }
    return mp;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public String getDescribe() {
    return describe;
  }
}
