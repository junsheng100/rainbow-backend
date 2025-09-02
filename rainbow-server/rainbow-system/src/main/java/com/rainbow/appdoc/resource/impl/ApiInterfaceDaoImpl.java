package com.rainbow.appdoc.resource.impl;

import com.rainbow.appdoc.entity.AppInterface;
import com.rainbow.appdoc.repository.ApiInterfaceRepository;
import com.rainbow.appdoc.resource.ApiInterfaceDao;
import com.rainbow.base.constant.CacheConstants;
import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * ApiInterface Dao实现
 */
@Repository
public class ApiInterfaceDaoImpl extends BaseDaoImpl<AppInterface, String, ApiInterfaceRepository> implements ApiInterfaceDao {

  public AppInterface check(AppInterface entity) {
    AppInterface old = getOne(entity);

    Integer disabled = entity.getDisabled();

    if(null == disabled){
      if(null == old){
        disabled = UseStatus.NO.getData();
      }else{
        disabled = null != old.getDisabled()?old.getDisabled():  UseStatus.NO.getData();
      }
    }
    entity.setDisabled(disabled);
    return old;
  }

  @CacheEvict(value = CacheConstants.CACHE_KEY_URL)
  @Override
  public AppInterface save(AppInterface entity) {
     return super.save(entity);
  }

  @CacheEvict(value = CacheConstants.CACHE_KEY_URL)
  @Override
  public AppInterface store(AppInterface entity) {
     return super.save(entity);
  }

  @Override
  public void saveList(String categoryId, List<AppInterface> list) {
    if (null == categoryId)
      Assert.notNull(categoryId, "categoryId can not be null");
    if (CollectionUtils.isNotEmpty(list)) {
      for (AppInterface method : list) {
        method.setCategoryId(categoryId);
        this.save(method);
      }
    }
  }

  @Override
  public List<AppInterface> findByCategoryId(String categoryId) {
    return StringUtils.isBlank(categoryId)?null:jpaRepository.findByCategoryId(categoryId);
  }

  @Override
  public List<AppInterface> findInCategoryIds(List<String> categoryIds) {
    return CollectionUtils.isEmpty(categoryIds)?null:jpaRepository.findInCategoryIds(categoryIds);
  }

  @Override
  public List<AppInterface> findNotInId(List<String> idList) {
    return CollectionUtils.isEmpty(idList)?null:jpaRepository.findNotInId(idList);
  }

  @Override
  public List<AppInterface> findAll() {
    return super.jpaRepository.findAll();
  }


  @CacheEvict(value = CacheConstants.CACHE_KEY_URL)
  @Override
  public Boolean delete(String id) {
    return super.remove(id);
  }

  @CacheEvict(value = CacheConstants.CACHE_KEY_URL)
  @Override
  public Boolean deleteInBatch(List<String> data) {
    return super.removeInBatch(data);
  }
}