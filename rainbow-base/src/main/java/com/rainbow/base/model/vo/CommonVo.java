package com.rainbow.base.model.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonVo <T> extends PageDomain {

  private T data;

}
