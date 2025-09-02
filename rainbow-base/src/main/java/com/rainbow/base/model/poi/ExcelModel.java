package com.rainbow.base.model.poi;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：QQ: 304299340
 * @Package：com.rainbow.base.model.poi
 * @name：ExcelModel 
 * @Filename：ExcelModel
 */
@Data
public class ExcelModel<T> implements Serializable {

  private String fileName;

  private String sheetName;

  private List<String> titles;

  private List<T> dataList;


}
