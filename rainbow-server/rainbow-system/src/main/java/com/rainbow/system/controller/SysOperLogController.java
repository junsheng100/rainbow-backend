package com.rainbow.system.controller;

import com.rainbow.base.controller.BaseController;
import com.rainbow.base.enums.TimeType;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.model.vo.OperLogVo;
import com.rainbow.system.entity.SysOperLog;
import com.rainbow.system.model.vo.OperLogData;
import com.rainbow.system.model.vo.OperLogMonthData;
import com.rainbow.system.model.vo.OperLogUserData;
import com.rainbow.system.service.SysOperLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/oper/log")
@Tag(name = "操作日志")
public class SysOperLogController extends BaseController<SysOperLog, Long, SysOperLogService> {


  @Operation(description = "统计高频的数据")
  @GetMapping("/total/top")
  public Result<List<OperLogData>> totalOperTopList(@RequestParam(name = "limit", defaultValue = "20") Integer limit,
                                                    @RequestParam(name = "desc", defaultValue = "ture", required = false) boolean desc) {
    return Result.success(service.totalOperTopList(limit, desc));
  }

  @Operation(description = "按月统计的数据")
  @GetMapping("/total/month")
  public Result<List<OperLogMonthData>> totalMonthList(@RequestParam(name = "start", required = false)
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                                                       @RequestParam(name = "end", required = false)
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                                                       @RequestParam(name = "top", defaultValue = "10") Integer top) {
    return Result.success(service.totalMonthList(start, end, top));
  }


  @Operation(description = "用户统计的数据")
  @GetMapping("/total/user")
  public Result<List<OperLogUserData>> totalUserList(@RequestParam(name = "start", required = false)
                                                     @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                                                     @RequestParam(name = "end", required = false)
                                                     @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                                                     @RequestParam(name = "type") TimeType type) {
    return Result.success(service.totalUserList(start, end, type));
  }


  @Operation(description = "接收日志")
  @PostMapping("/receive")
  public Result<Boolean> receive(@RequestBody OperLogVo vo) {
    return Result.success(service.receive(vo));
  }

  @Operation(description = "清理全部日志")
  @DeleteMapping("/clean/all")
  public Result<Boolean> cleanAll() {
    return Result.success(service.cleanAll());
  }

  @Override
  @Operation(description = "批量删除日志")
  @PostMapping("/batch/delete")
  public Result<Boolean> deleteInBatch(@RequestBody CommonVo<List<Long>> vo) {
    return Result.success(service.removeInBatch(vo.getData()));
  }


}
