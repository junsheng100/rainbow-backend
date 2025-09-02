package com.rainbow.template.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum JavaDataType {

  String("java.lang.String", "字符串 (String)"),
  Byte("java.lang.Byte", "字节型"),
  Short("java.lang.Short", "短整型 (Short)"),
  Integer("java.lang.Integer", "整型 (Integer)"),
  Float("java.lang.Float", "单精度浮点型 (Float)"),
  Double("java.lang.Double", "双精度浮点型 (Double)"),
  Boolean("java.lang.Boolean", "布尔型 (Boolean)"),
  Character("java.lang.Character", "字符型 (Character)"),
  BigInteger("java.math.BigInteger", "大整数 (BigInteger)"),
  BigDecimal("java.math.BigDecimal", "高精度数字 (BigDecimal)"),
  Date("java.util.Date", "日期 (Date)"),
  LocalDateTime("java.time.LocalDateTime", "日期时间 (LocalDateTime)"),
  ZonedDateTime("java.time.ZonedDateTime", "带时区的日期时间 (ZonedDateTime)"),
  BTYEARRAY("byte[]", "字节数组 (byte[])"),

          ;

  private String code;

  private String message;


  JavaDataType(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static JavaDataType getByCode(String code) {
    if (code == null) {
      return null;
    }
    for (JavaDataType JavaDataType : JavaDataType.values()) {
      if (JavaDataType.getCode().equals(code)) {
        return JavaDataType;
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
    for (JavaDataType type : JavaDataType.values()) {
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
    for (JavaDataType type : JavaDataType.values()) {
      mp.put(type.getCode(), type.getMessage());
    }
    return mp;
  }
}
