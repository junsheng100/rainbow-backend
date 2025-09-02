package com.rainbow.system.controller;

import com.rainbow.base.annotation.NoRepeatSubmit;
import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.system.entity.SysLogin;
import com.rainbow.system.model.vo.LoginData;
import com.rainbow.system.service.SysLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/login/user")
@Tag(name = "登陆日志")
public class SysLoginController extends BaseController<SysLogin, Long, SysLoginService> {

  @Operation(description = "按城市统计")
  @GetMapping("/total/city")
  public Result<List<LoginData>> totalArea() {
    return Result.success(service.totalAreaPro());
  }


  @OperLog
  @Operation(description = "删除清除")
  @DeleteMapping("/clean/all")
  public Result<Boolean> cleanAll() {
    return Result.success(service.cleanAll());
  }


  @OperLog
  @Override
  @Operation(description = "批量清除")
  @PostMapping("/batch/delete")
  public Result<Boolean> deleteInBatch(@RequestBody CommonVo<List<Long>> vo) {
    return Result.success(service.removeInBatch(vo.getData()));
  }

  @Operation(description = "查询在线用户")
  @PostMapping("/online")
  public Result<PageData<SysLogin>> findOnLine(@RequestBody CommonVo<String> vo) {
    return Result.success(service.findOnLine(vo));
  }


  @NoRepeatSubmit
  @OperLog("强制退出")
  @DeleteMapping("/exit/{id}")
  public Result<Boolean> logout(@PathVariable(name = "id") String userId) {
    return Result.success(service.logout(userId));
  }


  @NoRepeatSubmit
  @OperLog("批量强制退出")
  @PostMapping("/exit")
  public Result<Boolean> logoutInBatch(@RequestBody CommonVo<String[]> vo) {
    return Result.success(service.logout(vo.getData()));
  }


  @NoRepeatSubmit
  @OperLog("批量强制退出")
  @PostMapping("/exit/all")
  public Result<Boolean> logoutAll() {
    return Result.success(service.logoutAll());
  }


}
