package com.rainbow.user.model;

import com.rainbow.base.model.domain.TreeModel;
import lombok.Data;

@Data
public class MenuTree extends TreeModel<Long,MenuTree> {

  private String menuType;

}
