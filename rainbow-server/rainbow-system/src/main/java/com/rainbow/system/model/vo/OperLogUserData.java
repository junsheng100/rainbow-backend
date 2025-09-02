package com.rainbow.system.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.system.model.vo
 * @Filename：OperLogUserData
 * @Describe:
 */
@Data
public class OperLogUserData implements Serializable {

  private String time;
  private Long users;
  private Long count;

  public OperLogUserData() {
  }


  public OperLogUserData( String time,Long users, Long count) {
    this.users = users;
    this.time = time;
    this.count = count;
  }

}
