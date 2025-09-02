package com.rainbow.base.service;

import com.rainbow.base.api.ApiInfo;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.model.domain.LoginUser;

import javax.security.auth.message.AuthException;
import java.io.Serializable;
import java.util.List;

public interface BaseService<Entity extends BaseEntity,ID extends Serializable   > extends ApiInfo<Entity,ID> {

  LoginUser getLoginUser();

  boolean isAdmin() throws AuthException;

  boolean removeInBatch(List<ID> ids);

   Boolean deleteInBatch(List<ID> data);
}
