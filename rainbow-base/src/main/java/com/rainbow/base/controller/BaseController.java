package com.rainbow.base.controller;

import com.rainbow.base.annotation.NoRepeatSubmit;
import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.annotation.RestResponse;
import com.rainbow.base.api.ApiResult;
import com.rainbow.base.client.UserClient;
import com.rainbow.base.config.JwtConfig;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.domain.LoginUser;
import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.service.BaseService;
import com.rainbow.base.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@Data
@Slf4j
public class BaseController<Entity extends BaseEntity, ID extends Serializable, Service extends BaseService<Entity, ID>> implements ApiResult<Entity, ID> {

  @Autowired
  protected Service service;

  @Autowired
  protected HttpServletRequest request;

  @Autowired
  protected JwtConfig config;

  @Autowired
  protected JwtTokenUtil jwtTokenUtil;

  @Autowired
  protected UserClient userClient;


  @OperLog("详情")
  @Operation(description = "详情数据")
  @GetMapping("/{id}")
  @RestResponse
  @Override
  public Result<Entity> get(@PathVariable(name = "id") ID id) {
    Entity entity = service.get(id);
    return Result.success(entity);
  }

  @NoRepeatSubmit
  @OperLog("存储数据")
  @Operation(description = "存储数据")
  @RestResponse
  @PostMapping
  @Override
  public Result<Entity> store(@RequestBody @Valid Entity entity) {
    return Result.success(service.store(entity));
  }

  @NoRepeatSubmit
  @OperLog("删除")
  @Operation(description = "删除数据")
  @RestResponse
  @DeleteMapping("/{id}")
  @Override
  public Result<Boolean> delete(@PathVariable(name = "id") ID id) {
    return Result.success(service.delete(id));
  }

  @OperLog("列表查询")
  @RestResponse
  @Operation(description = "列表查询")
  @PostMapping("/list")
  @Override
  public Result<List<Entity>> list(@RequestBody BaseVo<Entity> vo) {
    return Result.success(service.list(vo));
  }


  @OperLog("分页查询")
  @RestResponse
  @Operation(description = "分页查询")
  @PostMapping("/page")
  @Override
  public Result<PageData<Entity>> page(@RequestBody BaseVo<Entity> vo) {
    PageData<Entity> page = service.page(vo);
    return Result.success(page);
  }


  @OperLog("批量删除")
  @Operation(description = "批量删除")
  @RestResponse
  @PostMapping("/batch/delete")
  public Result<Boolean> deleteInBatch(@RequestBody CommonVo<List<ID>> vo) {
    Boolean flag = service.deleteInBatch(vo.getData());
    return Result.success(flag);
  }


  protected boolean isToken() throws AuthException {
    String token = request.getHeader(config.getHeader());

    log.info("###### profile:", token);

    // 验证令牌
    Assert.notNull(token, "无效的令牌");
    if (StringUtils.isBlank(token))
      throw new AuthException("无效的令牌");
    // 使用统一的方法处理 token

    if (!jwtTokenUtil.validateToken(token))
      throw new AuthException("无效的令牌");

    return true;
  }

  protected LoginUser getLoginUser() {
    LoginUser user = userClient.getLoginUser();
    return user;
  }


  protected String getUserId() {
    LoginUser user = userClient.getLoginUser();
    return user.getUserId();
  }
}
