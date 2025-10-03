package com.rainbow.appdoc.service;

import com.rainbow.appdoc.entity.AppInterface;
import com.rainbow.appdoc.model.InterfaceModel;
import com.rainbow.base.service.BaseService;

import java.util.List;
import java.util.Map;

public interface ApiInterfaceService extends BaseService<AppInterface, String> {


  List<AppInterface> findByCategoryId(String categoryId);

   Map<String, InterfaceModel> getMapUrlList();
}