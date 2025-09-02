package com.rainbow.appdoc.resource;

import com.rainbow.appdoc.entity.AppCategory;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.resource.BaseDao;

import java.util.List;

public interface ApiCategoryDao extends BaseDao<AppCategory, String> {
  PageData<AppCategory> findByKeyword(CommonVo<String> vo);

  PageData<AppCategory> findMenuByKeyword(CommonVo<String> vo);

  List<AppCategory> findAll();

  List<AppCategory> findNotInId(List<String> idList);
}