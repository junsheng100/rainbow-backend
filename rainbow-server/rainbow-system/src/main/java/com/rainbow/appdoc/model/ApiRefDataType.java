package com.rainbow.appdoc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.appInfo.model
 * @Filename：ApiRefDataType
 * @Date：2025/7/28 18:34
 * @Describe:
 */
@Data
public class ApiRefDataType implements Serializable {

  /** 关联控制类ID */
  private String categoryId;

  private String interfaceId;

  private String modelTypeId;

  private String className;

  private String methodName;

  private String typeName;

  /** 枚举 DataTypeEnums 0:出参,1:入参*/
  private String dataType;

  /** 数据格式（JSON格式） */
  private String data;

  private String md5Code;

}
