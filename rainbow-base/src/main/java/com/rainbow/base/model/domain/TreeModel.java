package com.rainbow.base.model.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TreeModel<ID extends Serializable,T extends TreeModel> {

  private ID id;

  private String label;

  private ID parentId;

  private Integer orderNo;

  private List<T> children;

  public TreeModel() {
  }


  public TreeModel(ID id, String label, ID parentId, Integer orderNo) {
    this.id = id;
    this.label = label;
    this.parentId = parentId;
    this.orderNo = orderNo;
  }
}
