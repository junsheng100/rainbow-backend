package com.rainbow.base.api;

import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.BaseVo;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

public interface ApiInfo<Entity extends BaseEntity,ID extends Serializable> {

  //单条数据查询
  Entity get(ID id);
  //数据存储（新增/更新）
  Entity store(@Valid Entity entity);
  Entity save(@Valid Entity entity);
  // 逻辑删除
  Boolean delete(ID id);
  // 集合查询
  List<Entity> list(BaseVo<Entity> vo);
  // 分页查询
  PageData<Entity> page(BaseVo<Entity> vo);


}
