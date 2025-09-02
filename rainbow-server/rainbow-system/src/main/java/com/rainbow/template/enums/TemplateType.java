package com.rainbow.template.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum TemplateType {

  Entity("Entity", "实体类型"),
  Controller("Controller", "控制器类型"),
  Repository("Repository", "数据操作类"),
  Dao("Dao", "数据管理接口类"),
  DaoImpl("DaoImpl", "数据管理接口实现"),
  Service("Service", "服务定义类"),
  ServiceImpl("ServiceImpl", "服务实现类"),

  ;

  private String code;

  private String message;


  TemplateType(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static TemplateType getByCode(String code) {
    if (code == null) {
      return null;
    }
    for (TemplateType TemplateType : TemplateType.values()) {
      if (TemplateType.getCode().equals(code)) {
        return TemplateType;
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
    for (TemplateType type : TemplateType.values()) {
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
    for (TemplateType type : TemplateType.values()) {
      mp.put(type.getCode(), type.getMessage());
    }
    return mp;
  }
  
}
