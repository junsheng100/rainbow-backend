package com.rainbow.monitor.model.dto;

import com.rainbow.base.model.domain.TreeModel;
import lombok.Data;

@Data
public class CacheTree extends TreeModel<String, CacheTree> {

  private String key;

  private String value;
}
