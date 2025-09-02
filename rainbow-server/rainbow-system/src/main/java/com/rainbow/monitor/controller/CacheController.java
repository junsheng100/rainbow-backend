package com.rainbow.monitor.controller;

import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.model.base.Result;
import com.rainbow.monitor.model.dto.CacheTree;
import com.rainbow.monitor.service.MonitorCacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "缓存信息", description = "缓存信息")
@RequestMapping("/monitor/cache")
public class CacheController {

  @Autowired
  private MonitorCacheService service;


  @OperLog
  @GetMapping("/tree")
  @Operation(description = "获取缓存树结构数据")
  public Result<List<CacheTree>> getTreeInfo() throws Exception {
    List<CacheTree> result = service.getTreeInfo();
    return Result.success(result);
  }

  @OperLog
  @GetMapping
  @Operation(description = "获取缓数据")
  public Result<Map<String,Object>> getInfo() throws Exception {
    return Result.success(service.getInfo());
  }

}
