package com.rainbow.system.controller;

import com.rainbow.base.annotation.NoRepeatSubmit;
import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.model.base.Result;
import com.rainbow.system.entity.SysNoticePush;
import com.rainbow.system.model.dto.NoticePushDto;
import com.rainbow.system.service.SysNoticePushService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/notice/push")
@Tag(name = "通知/公告 推送")
public class SysNoticePushController extends BaseController<SysNoticePush,String, SysNoticePushService> {


  @GetMapping("/will/total")
  @Operation(description = "待推送统计数据")
  public Result<Integer> getWillReadCount() {

    Integer result = service.getWillReadCount();
    return Result.success(result);
  }

  @NoRepeatSubmit
  @OperLog("生成推送计划")
  @PostMapping("/plan")
  @Operation(description = "生成推送计划")
  public Result<Boolean> savePlanData(@RequestBody @Valid NoticePushDto vo) {
    Boolean result = service.savePlanData(vo);
    return Result.success(result);
  }


  @NoRepeatSubmit
  @OperLog("存储推送数据")
  @PostMapping
  @Override
  public Result<SysNoticePush> store(@RequestBody @Valid SysNoticePush entity) {
    throw new BizException("暂不支持编辑操作");
  }

}
