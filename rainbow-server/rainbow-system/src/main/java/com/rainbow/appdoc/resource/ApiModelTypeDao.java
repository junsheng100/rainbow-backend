package com.rainbow.appdoc.resource;

import com.rainbow.appdoc.entity.AppModelType;
import com.rainbow.base.resource.BaseDao;

import java.util.List;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.appInfo.resource.impl
 
 * @name：ApiModelTypeDao
 * @Date：2025/7/27 11:43
 * @Filename：ApiModelTypeDao
 */
public interface ApiModelTypeDao extends BaseDao<AppModelType,String> {


  void saveList(List<AppModelType> typeList);
}
