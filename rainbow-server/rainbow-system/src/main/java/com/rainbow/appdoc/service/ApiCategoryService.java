package com.rainbow.appdoc.service;

import com.rainbow.appdoc.entity.AppCategory;
import com.rainbow.appdoc.model.CategoryModel;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.service.BaseService;

import java.util.List;

public interface ApiCategoryService extends BaseService<AppCategory, String> {




  Boolean init();


  void initData();


  AppCategory create(String className);

  List<Class<?>> getAppCategoryClassList();


  PageData<AppCategory> findByKeyword(CommonVo<String> vo);

  PageData<CategoryModel> findMenus(CommonVo<String> vo);

}