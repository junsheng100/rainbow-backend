package com.rainbow.base.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;

@Data
public class FieldModel implements Serializable {

  private String name;

  private String type;

  private Boolean isGeneric;

  private Class<?> src;

  private Field field;

  private Boolean isCollection;

  private Boolean isMap;


  public FieldModel() {

  }

  public FieldModel(String name, String type) {
    this.name = name;
    this.type = type;
  }

  public FieldModel(String name, String type,Boolean isGeneric) {
    this.name = name;
    this.type = type;
    this.isGeneric = isGeneric;
  }

}
