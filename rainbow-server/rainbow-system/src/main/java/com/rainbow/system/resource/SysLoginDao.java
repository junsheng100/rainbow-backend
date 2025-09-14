package com.rainbow.system.resource;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.resource.BaseDao;
import com.rainbow.system.entity.SysLogin;
import com.rainbow.system.model.vo.LogParamVo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SysLoginDao extends BaseDao<SysLogin,Long> {
  Boolean deleteAll();


  List<Object[]> totalAreaPro();

  List<SysLogin> findTheDay(String theDay);

  Long countLogin(String theDay);


  PageData<SysLogin> pageList(LogParamVo data, Pageable pageable);
}
