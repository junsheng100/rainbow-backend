package com.rainbow.user.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PasswordDto implements Serializable {

  private String oldPassword;
  private String newPassword;
}
