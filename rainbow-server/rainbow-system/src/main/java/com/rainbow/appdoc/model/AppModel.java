package com.rainbow.appdoc.model;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.appInfo.model
 * @name：TreeModel
 * @Date：2025/7/25 17:03
 * @Filename：TreeModel
 */
@Data
public class AppModel implements Serializable {

  private String id;

  private Type type;

  private Object child;

  public AppModel() {
  }

  public AppModel(String id, Type type) {
    this.id = id;
    this.type = type;
  }

  public AppModel(String id, Type type, List<AppModel> child) {
    this.id = id;
    this.type = type;
    this.child = child;
  }


}
