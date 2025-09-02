package com.rainbow.user.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RightDto implements Serializable {

  private Long roleId;

  private Long menuId;

}
