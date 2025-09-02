package com.rainbow.scheduler.controller;

import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.Result;
import com.rainbow.scheduler.entity.TaskConfig;
import com.rainbow.scheduler.entity.TaskConfigParams;
import com.rainbow.scheduler.service.TaskConfigParamsService;
import com.rainbow.scheduler.service.TaskConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务配置控制器
 */
@RestController
@RequestMapping("/task/config")
@Tag(name = "任务-设置")
public class TaskConfigController extends BaseController<TaskConfig, String, TaskConfigService> {

  @Autowired
  private TaskConfigParamsService paramsService;

  @OperLog
  @GetMapping("/params/{configId}")
  @Operation(description = "按任务设置ID查询参数列表")
  public Result<List<TaskConfigParams>> findByConfigId(@PathVariable String configId) {
    List<TaskConfigParams> list = paramsService.findByConfigId(configId);
    return Result.success(list);
  }

  @OperLog
  @PostMapping("/start/{id}")
  @Operation(description = "按任务ID:启动")
  public Result<Boolean> startTask(@PathVariable String id) {
    service.startTask(id);
    return Result.success(Boolean.TRUE);
  }

  @OperLog
  @PostMapping("/stop/{id}")
  @Operation(description = "按任务ID:停止")
  public Result<Boolean> stopTask(@PathVariable String id) {
    service.stopTask(id);
    return Result.success(Boolean.TRUE);
  }

  @OperLog
  @PostMapping("/execute/once/{id}")
  @Operation(description = "按任务ID:执行1次")
  public Result<Boolean> executeOnce(@PathVariable String id) {
    service.executeOnce(id);
    return Result.success(Boolean.TRUE);
  }

  @OperLog
  @GetMapping("/validate/cron")
  @Operation(description = "验证cron数据")
  public Result<Boolean> validateCronExpression(@RequestParam String cronExpression) {
    Boolean valid = service.validateCronExpression(cronExpression);
    return Result.success(valid);
  }

}