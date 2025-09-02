package com.rainbow.user.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserTotal implements Serializable {
   

  private Long total ; // 总用户数
  private Long login ; // 今日登录
  private Long online ; // 在线用户

}
