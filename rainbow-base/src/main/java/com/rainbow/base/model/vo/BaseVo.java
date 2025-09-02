package com.rainbow.base.model.vo;

import com.rainbow.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class BaseVo <T extends BaseEntity> extends  PageDomain {


  private T data;

  private Map<String,Object> map;

  public BaseVo() {
  }

  public BaseVo(T entity) {
    this.data = entity;
  }


}
