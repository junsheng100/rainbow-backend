package com.rainbow.appdoc.service.impl;

import com.rainbow.appdoc.entity.AppCategory;
import com.rainbow.appdoc.entity.AppInterface;
import com.rainbow.appdoc.model.InterfaceModel;
import com.rainbow.appdoc.resource.ApiCategoryDao;
import com.rainbow.appdoc.resource.ApiInterfaceDao;
import com.rainbow.appdoc.service.ApiInterfaceService;
import com.rainbow.base.constant.CacheConstants;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.base.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ApiInterface Service实现
 */
@Service
public class ApiInterfaceServiceImpl extends BaseServiceImpl<AppInterface, String, ApiInterfaceDao> implements ApiInterfaceService {

  @Autowired
  private ApiCategoryDao categoryDao;

  @Override
  public List<AppInterface> findByCategoryId(String categoryId) {
    return super.baseDao.findByCategoryId(categoryId);
  }

  @Cacheable(value = CacheConstants.CACHE_KEY_URL)
  @Override
  public List<InterfaceModel> getUrlList() {

    List<InterfaceModel> list = new ArrayList<>();
    List<AppCategory> categoryList = categoryDao.findAll();
    List<AppInterface> interfaceList = baseDao.findAll();

    if (CollectionUtils.isNotEmpty(interfaceList) && CollectionUtils.isNotEmpty(categoryList)) {
      interfaceList.stream().forEach(item -> {
        AppCategory category = categoryList.stream().filter(t -> t.getId().equals(item.getCategoryId())).findFirst().orElse(null);
        if (null != category) {
          InterfaceModel model = new InterfaceModel();
          BeanUtils.copyProperties(item, model, CommonUtils.getNullPropertyNames(item));
          String url = category.getRequestUrl() + "/" + item.getRequestUrl();
          url = url.replace("//", "/");
          model.setRequestUrl(url);
          list.add(model);
        }

      });
    }

    return list;
  }
}