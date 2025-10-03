package com.rainbow.user.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.user.model
 * @Filename：UserProfile
 * @Date：2025/9/11 20:12
 * @Describe:
 */
@Data
public class UserProfile implements Serializable {

  private Long deptId;

  private String deptName;

  private String userId;

  private String nickname;

  private String username;

  private String avatar;
}
