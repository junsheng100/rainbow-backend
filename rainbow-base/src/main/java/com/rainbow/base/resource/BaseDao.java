package com.rainbow.base.resource;

import com.rainbow.base.api.ApiInfo;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.model.domain.LoginUser;
import com.rainbow.base.model.vo.BaseVo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseDao<Entity extends BaseEntity,ID extends Serializable> extends ApiInfo<Entity,ID>  {


  Entity check(Entity entity);

  Boolean remove(ID id);
  Boolean removeInBatch(List<ID> idList);

  Map<ID,Entity> findMapInId(List<ID> idList,Class<Entity> clazz);

  Boolean deleteInBatch(List<ID> data);

  Sort getBizSort(BaseVo<Entity> vo);

  Pageable getPageRequest(BaseVo<Entity> vo);

  LoginUser getLoginUser();
}
