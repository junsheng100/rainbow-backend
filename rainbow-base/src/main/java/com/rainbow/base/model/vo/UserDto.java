package com.rainbow.base.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {

  private String userId;
  private String userName;
  private String userType;
  private String nickname;

}
