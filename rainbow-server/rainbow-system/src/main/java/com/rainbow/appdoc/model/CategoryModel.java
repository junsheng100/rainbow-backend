package com.rainbow.appdoc.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.appdoc.model
 * @Filename：CategoryVo
 * @Date：2025/8/7 10:17
 * @Describe:
 */
@Data
public class CategoryModel implements Serializable {


  private String className;

  private String simpleName;

  private String requestUrl;

  private String description;

  private List<InterfaceModel> interfaceModelList;


}
