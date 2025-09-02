package com.rainbow.base.resource.impl;

import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.exception.DataException;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.domain.LoginUser;
import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.base.repository.BaseRepository;
import com.rainbow.base.resource.BaseDao;
import com.rainbow.base.utils.BeanTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.util.Assert;

import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class BaseDaoImpl<Entity extends BaseEntity, ID extends Serializable, Repository extends BaseRepository<Entity, ID>> implements BaseDao<Entity, ID> {
  @Autowired
  protected Repository jpaRepository;

  @Autowired
  protected DataManager<Entity> dataManager;


  @Override
  public Entity get(ID id) {
    if (null == id) {
      throw new DataException("id  不能为空");
    }
    try {
      Optional<Entity> optional = jpaRepository.findById(id);
      Entity entity = optional.orElse(null);
      return entity;
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
      return null;
    }
  }

  @Override
  public Entity store(@Valid Entity entity) {
    try {
      if (null == entity) {
        throw new DataException("参数不能为空");
      }

      Entity old = check(entity);
      if (null == old) {
        dataManager.addEntity(entity);
      } else {
        dataManager.modEntity(entity, old);
      }
      jpaRepository.save(entity);
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
      throw new DataException(e.getMessage());
    }

    return entity;
  }

  @Override
  public Entity save(Entity entity) {

    try {
      if (null == entity) {
        throw new DataException("参数不能为空");
      }
      Entity old = check(entity);
      if (null == old) {
        dataManager.addNormal(entity);
      } else {
        dataManager.modNormal(entity, old);
      }
      jpaRepository.save(entity);
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
      throw new DataException(e.getMessage());
    }
    return entity;
  }

  @Override
  public Boolean delete(ID id) {
    Optional<Entity> optional = jpaRepository.findById(id);
    Entity entity = optional.orElse(null);
    if (null != entity) {
      entity.setStatus(UseStatus.DEL.getCode());
      entity.setLcd(LocalDateTime.now());
      jpaRepository.save(entity);
      return true;
    }
    throw new DataException("数据不存在");
  }

  @Override
  public List<Entity> list(BaseVo<Entity> vo) {
    Sort sort =  getBizSort(vo);
    List<Entity> list = jpaRepository.findAll((root, query, cb) -> {
      Map<String, Predicate> mp = commonParams(root, cb, vo);
      List<Predicate> predicateList = dataManager.getCommonPredicates(mp, root, cb, vo);
      return cb.and((predicateList.toArray(new Predicate[predicateList.size()])));
    }, sort);
    return list;
  }




  @Override
  public PageData<Entity> page(BaseVo<Entity> vo) {

    Page<Entity> page = new PageImpl<>(new ArrayList<>());
    try {
      Pageable pageable = getPageRequest(vo);

      page = jpaRepository.findAll((root, query, cb) -> {
        Map<String, Predicate> mp = commonParams(root, cb, vo);
        List<Predicate> predicateList = dataManager.getCommonPredicates(mp, root, cb, vo);
        return cb.and((predicateList.toArray(new Predicate[predicateList.size()])));
      }, pageable);
    } catch (DataException e) {
      log.error(e.getMessage());
    }
    return new PageData<>(page);
  }

  public Sort getBizSort(BaseVo<Entity> vo) {
    return dataManager.getBizSort(vo);
  }

  public Pageable getPageRequest(BaseVo<Entity> vo) {
    return dataManager.getPageRequest(vo);
  }


  protected Map<String, Predicate> commonParams(Root<Entity> root, CriteriaBuilder cb, BaseVo<Entity> vo) {
    return dataManager.getPredicates(root, cb, vo);
  }

  public Entity check(Entity entity) {
    Entity old = getOne(entity);
    return old;
  }

  public Entity getOne(Entity entity) {
    Entity old = null;
    try {
      old = getOne(entity, Id.class);
      if (null == old) {
        old = getOne(entity, UnionKey.class);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new DataException(e.getMessage());
    }

    return old;
  }

  public Entity getOne(Entity entity, Class clzz) {
    Entity old = null;
    try {
      Entity next = BeanTools.clonePk(entity, clzz);
      if (null == next)
        return null;
      Example<Entity> example = Example.of(next);
      Optional<Entity> optional = jpaRepository.findOne(example);
      old = optional.orElse(null);
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
      throw new DataException(e.getMessage());
    }

    return old;
  }

  @Override
  public Boolean remove(ID id) {
    Entity entity = get(id);
    Assert.notNull(entity, "数据不存在");
    jpaRepository.delete(entity);
    return true;
  }

  @Override
  public Boolean removeInBatch(List<ID> idList) {
    try {
      if (CollectionUtils.isNotEmpty(idList)) {
        jpaRepository.deleteAllById(idList);
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
      throw new DataException(e.getMessage());
    }
    return true;
  }

  @Override
  public Map<ID, Entity> findMapInId(List<ID> idList, Class<Entity> clazz) {
    Map<ID, Entity> map = new HashMap<>();

    List<Entity> list = jpaRepository.findAllById(idList);
    if (CollectionUtils.isNotEmpty(list)) {
      for (Entity entity : list) {
        ID id =  dataManager.getIdValue(entity, clazz);
        map.put(id, entity);
      }
    }

    return map;
  }

  @Override
  public Boolean deleteInBatch(List<ID> idList) {

    if (CollectionUtils.isNotEmpty(idList)) {
      List<Entity> list = jpaRepository.findAllById(idList);
      if (CollectionUtils.isNotEmpty(list)) {
        for (Entity entity : list) {
          entity.setStatus(UseStatus.DEL.getCode());
          entity.setLcd(LocalDateTime.now());
          jpaRepository.save(entity);
        }
      }
    }

    return null;
  }

  @Override
  public LoginUser getLoginUser() {
    return dataManager.getLoginUser();
  }

  protected void saveList(List<Entity> list) {

    if (CollectionUtils.isEmpty(list))
      return;
    for (Entity entity : list) {
      store(entity);
    }
  }

  protected String getUserName() {
    return dataManager.getUserName();
  }


}
