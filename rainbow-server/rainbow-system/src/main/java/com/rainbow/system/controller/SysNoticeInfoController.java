package com.rainbow.system.controller;

import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.annotation.RestResponse;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.vo.PageDomain;
import com.rainbow.system.entity.SysNoticeInfo;
import com.rainbow.system.service.SysNoticeInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/notice/info")
@Tag(name = "通知/公告")
public class SysNoticeInfoController extends BaseController<SysNoticeInfo,Long, SysNoticeInfoService> {

  @OperLog("查看详情")
  @PostMapping("read/{id}")
  @RestResponse
  @Operation(description = "查看详情")
  public Result<SysNoticeInfo> read(@PathVariable(name = "id") Long id) {
    SysNoticeInfo entity = service.read(id);
    return Result.success(entity);
  }

  @OperLog("分页查询未读")
  @RestResponse
  @PostMapping("/page/will")
  @Operation(description = "分页查询未读数据")
  public Result<PageData<SysNoticeInfo>> pageWill(@RequestBody PageDomain vo) {
    PageData<SysNoticeInfo> page = service.pageWill(vo);
    return Result.success(page);
  }


}
