package com.rainbow.appdoc.service;

import com.rainbow.appdoc.entity.AppInterface;
import com.rainbow.appdoc.model.InterfaceModel;
import com.rainbow.base.service.BaseService;

import java.util.List;

public interface ApiInterfaceService extends BaseService<AppInterface, String> {


  List<AppInterface> findByCategoryId(String categoryId);

  List<InterfaceModel> getUrlList();
}