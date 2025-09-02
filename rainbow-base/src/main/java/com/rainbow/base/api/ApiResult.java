package com.rainbow.base.api;

import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.base.model.vo.CommonVo;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

public interface ApiResult<Entity extends BaseEntity,ID extends Serializable> {

  //单条数据查询
  Result<Entity> get(ID id);
  //数据存储（新增/更新）
  Result<Entity> store(@Valid Entity entity);
  // 删除
  Result<Boolean> delete(ID id);
  // 批量删除
  Result<Boolean> deleteInBatch(@RequestBody CommonVo<List<ID>> vo);

  // 集合查询
  Result<List<Entity>> list(BaseVo<Entity> vo);
  // 分页查询
  Result<PageData<Entity>> page(BaseVo<Entity> vo);

}
