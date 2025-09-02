package com.rainbow.appdoc.resource.impl;

import com.rainbow.appdoc.entity.AppCategory;
import com.rainbow.appdoc.repository.ApiCategoryRepository;
import com.rainbow.appdoc.resource.ApiCategoryDao;
import com.rainbow.base.constant.CacheConstants;
import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.List;


/**
 * ApiCategory Dao实现
 */
@Repository
public class ApiCategoryDaoImpl extends BaseDaoImpl<AppCategory, String, ApiCategoryRepository> implements ApiCategoryDao {

  public AppCategory check(AppCategory entity) {
    AppCategory old = getOne(entity);


    Integer disabled = entity.getDisabled();

    if(null == disabled){
      if(null == old){
        disabled = UseStatus.NO.getData();
      }else{
        disabled = null != old.getDisabled()?old.getDisabled():  UseStatus.NO.getData();
      }
    }

    Integer orderNum = entity.getOrderNum();

    if(null == orderNum){
      if(null != old && null != old.getOrderNum()){
        orderNum = old.getOrderNum();
      }else{
        orderNum = 9999;
      }
    }

    entity.setDisabled(disabled);
    entity.setOrderNum(orderNum);

    return old;
  }


  @CacheEvict(value = CacheConstants.CACHE_KEY_URL)
  @Override
  public AppCategory store(@Valid AppCategory entity) {
    return super.store(entity);
  }
  @CacheEvict(value = CacheConstants.CACHE_KEY_URL)
  @Override
  public AppCategory save(@Valid AppCategory entity) {
    return super.save(entity);
  }




  @Override
  public PageData<AppCategory> findByKeyword(CommonVo<String> vo) {
    String keyword = vo.getData();

    Pageable pageable = dataManager.getCommonPageable(vo, AppCategory.class);

    Page<AppCategory> page = super.jpaRepository.findByKeyword(keyword,pageable);

    return new PageData<>(page);
  }

  @Override
  public PageData<AppCategory> findMenuByKeyword(CommonVo<String> vo) {
    String keyword = vo.getData();

    Pageable pageable = dataManager.getCommonPageable(vo, AppCategory.class);

    Page<AppCategory> page = super.jpaRepository.findMenuByKeyword(keyword,pageable);

    return new PageData<>(page);
  }

  @Override
  public List<AppCategory> findAll() {
    return super.jpaRepository.findAll();
  }

  @Override
  public List<AppCategory> findNotInId(List<String> idList) {
    return CollectionUtils.isEmpty(idList)?null:super.jpaRepository.findNotInId(idList);
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