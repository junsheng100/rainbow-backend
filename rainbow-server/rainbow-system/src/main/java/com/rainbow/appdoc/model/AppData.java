package com.rainbow.appdoc.model;

import com.rainbow.appdoc.entity.AppCategory;
import com.rainbow.appdoc.entity.AppInterface;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.appInfo.model
 
 * @name：ApiData
 * @Date：2025/7/19 11:04
 * @Filename：ApiData
 */
@Data
public class AppData implements Serializable {


  private AppCategory category;

  private List<AppInterface> interfaces;


  public AppData() {
  }



  public AppData(AppCategory appCategory, List<AppInterface> interfaces) {
    this.category = appCategory;
    this.interfaces = interfaces;
  }
}
