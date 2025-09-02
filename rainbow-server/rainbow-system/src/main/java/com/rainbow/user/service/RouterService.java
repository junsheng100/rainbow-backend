package com.rainbow.user.service;

import com.rainbow.base.model.router.RouterVo;
import com.rainbow.user.entity.SysMenu;

import java.util.List;

public interface RouterService {

  List<RouterVo> getRouters();

 List<SysMenu> findMenuListByUserId(String userId);

}
