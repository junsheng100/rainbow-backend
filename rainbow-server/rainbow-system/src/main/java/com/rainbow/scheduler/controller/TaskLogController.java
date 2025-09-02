package com.rainbow.scheduler.controller;

import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.vo.TaskLogVo;
import com.rainbow.scheduler.entity.TaskLog;
import com.rainbow.scheduler.service.TaskLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 任务日志控制器
 */
@RestController
@RequestMapping("/task/log")
@Tag(name = "任务-日志")
public class TaskLogController extends BaseController<TaskLog, String, TaskLogService> {



  @PostMapping("/receive")
  @Operation(description = "接收任务日志数据")
  public Result<Boolean> receive(@RequestBody TaskLogVo vo) {
    TaskLog taskLog = service.receive(vo);
    return Result.success(null != taskLog);
  }


  @PostMapping("/clean")
  @Operation(description = "清除数据")
  public ResponseEntity<Void> cleanLogs(
          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date beforeTime) {
    service.cleanLogs(beforeTime);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/export")
  @Operation(description = "导出日志数据")
  public ResponseEntity<byte[]> exportLogs(
          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
    byte[] data = service.exportLogs(startTime, endTime);
    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=task_logs.xlsx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(data);
  }
} 