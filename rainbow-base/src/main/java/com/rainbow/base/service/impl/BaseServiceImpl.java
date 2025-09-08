package com.rainbow.base.service.impl;

import com.rainbow.base.client.UserClient;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.UserType;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.exception.NoLoginException;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.domain.LoginUser;
import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.base.resource.BaseDao;
import com.rainbow.base.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BaseServiceImpl<Entity extends BaseEntity, ID extends Serializable, DAO extends BaseDao<Entity, ID>> implements BaseService<Entity, ID> {

  @Autowired
  protected DAO baseDao;

  @Autowired
  protected UserClient userClient;

  @Override
  public Entity get(ID id) {
    Entity entity = baseDao.get(id);
    convertData(entity);
    return entity;
  }

  @Override
  public Entity store(@Valid Entity entity) {
    if (validate(entity)) {
      handleData(entity);
      return baseDao.store(entity);
    }
    throw new BizException("数据验证失败");
  }


  @Override
  public Entity save(@Valid Entity entity) {
    if (validate(entity)) {
      handleData(entity);
      return baseDao.save(entity);
    }
    throw new BizException("数据验证失败");
  }

  @Override
  public Boolean delete(ID id) {
    return baseDao.delete(id);
  }

  @Override
  public List<Entity> list(BaseVo<Entity> vo) {
    parameters(vo);
    List<Entity> list = baseDao.list(vo);
    convertCollection(list);
    return CollectionUtils.isNotEmpty(list) ? list : new ArrayList<>();
  }

  @Override
  public PageData<Entity> page(BaseVo<Entity> vo) {
    parameters(vo);
    PageData<Entity> page = baseDao.page(vo);
    convertCollection(page.getContent());
    return page;
  }

  public void parameters(BaseVo<Entity> vo) {

  }

  public void handleData(@Valid Entity entity) {

  }


  public void convertData(Entity entity) {

  }

  public void convertCollection(List<Entity> list) {

  }

  public boolean validate(@Valid Entity entity) {

    return true;
  }

  public LoginUser getLoginUser() {
    return userClient.getLoginUser();
  }

  public boolean isAdmin()  {
    LoginUser user = getLoginUser();
    if (null == user)
      throw new NoLoginException("请登录系统");

    return UserType.ADMIN.name().equals(user.getUserType());
  }




  @Override
  public boolean removeInBatch(List<ID> ids) {
    return baseDao.removeInBatch(ids);
  }

  @Override
  public Boolean deleteInBatch(List<ID> data) {
    return baseDao.deleteInBatch(data);
  }
}
