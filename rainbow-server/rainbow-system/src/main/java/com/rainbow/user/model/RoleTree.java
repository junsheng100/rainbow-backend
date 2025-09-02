package com.rainbow.user.model;

import com.rainbow.base.model.domain.TreeModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleTree extends TreeModel<Long,RoleTree> {

  private List<Long> roleId;
  private boolean checked;

}
