package com.rainbow.system.model.vo;

import lombok.Data;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.system.model.vo
 * @Filename：LoginData
 * @Describe:
 */
@Data
public class LoginData {

  private String country;
  private String pro;
  private String city;
  private Long total;

  public LoginData(){

  }
  public LoginData(String country,String pro,String city, Long total){
    this.country = country;
    this.pro = pro;
    this.city = city;
    this.total = total;
  }
}
