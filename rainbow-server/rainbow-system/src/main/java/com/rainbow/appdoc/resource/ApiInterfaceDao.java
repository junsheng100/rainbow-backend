package com.rainbow.appdoc.resource;

import com.rainbow.appdoc.entity.AppInterface;
import com.rainbow.base.resource.BaseDao;

import java.util.List;

public interface ApiInterfaceDao extends BaseDao<AppInterface, String> {


  void saveList(String categoryId ,List<AppInterface> appInterfaces);


  List<AppInterface> findByCategoryId(String id);

  List<AppInterface> findInCategoryIds(List<String> categoryIds);

  List<AppInterface> findNotInId(List<String> interfaceIdList);


  List<AppInterface> findAll();
}