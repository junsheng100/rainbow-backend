package com.rainbow.appdoc.resource.impl;

import com.rainbow.appdoc.entity.AppModelType;
import com.rainbow.appdoc.repository.ApiModelTypeRepository;
import com.rainbow.appdoc.resource.ApiModelTypeDao;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.appInfo.resource.impl
 * @Filename：ApiModelTypeDaoImpl
 * @Date：2025/7/27 11:53
 */
@Slf4j
@Component
public class ApiModelTypeDaoImpl extends BaseDaoImpl<AppModelType, String, ApiModelTypeRepository> implements ApiModelTypeDao {

  @Override
  public void saveList(List<AppModelType> typeList) {
    if(CollectionUtils.isNotEmpty(typeList)){
      for(AppModelType entity:typeList){
        save(entity);
      }
    }
  }
}
