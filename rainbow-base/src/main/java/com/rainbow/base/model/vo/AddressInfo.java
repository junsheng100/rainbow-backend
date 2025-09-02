package com.rainbow.base.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.base.model.vo
 * @Filename：AddressVo
 * @Describe:
 */
@Data
public class AddressInfo implements Serializable {

  private String ip;
  private String country;
  private String city;
  private String pro;
  private String cityCode;
  private String addr;

}
