package com.rainbow.monitor.controller;

import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.annotation.RestResponse;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.monitor.entity.*;
import com.rainbow.monitor.model.server.ServerInfo;
import com.rainbow.monitor.model.vo.DataVo;
import com.rainbow.monitor.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 服务器监控
 *
 * @author rainvom
 */
@Slf4j
@RestController
@Tag(name = "服务器相关信息", description = "服务器相关信息")
@RequestMapping("/monitor/server")
public class ServerInfoController extends BaseController<SysData,String, SysDataService> {

    @Autowired
    private CpuDataService cpuDataService;
    @Autowired
    private DiskDataService diskDataService;
    @Autowired
    private JvmDataService jvmDataService;
    @Autowired
    private MemDataService memDataService;

    @OperLog
    @RestResponse
    @Operation(description = "分页查询Cpu数据")
    @PostMapping("/page/cpu")
    public Result<PageData<CpuData>> findPageCpu(@RequestBody CommonVo<DataVo> vo) {
        PageData<CpuData> page = cpuDataService.pageData(vo);
        return Result.success(page);
    }

    @OperLog
    @RestResponse
    @Operation(description = "分页查询Disk数据")
    @PostMapping("/page/disk")
    public Result<PageData<DiskData>> findPageDiskData(@RequestBody CommonVo<DataVo> vo) {
        PageData<DiskData> page = diskDataService.pageData(vo);
        return Result.success(page);
    }


    @OperLog
    @RestResponse
    @Operation(description = "分页查询Jvm数据")
    @PostMapping("/page/jvm")
    public Result<PageData<JvmData>> findPageJvmData(@RequestBody CommonVo<DataVo> vo) {
        PageData<JvmData> page = jvmDataService.pageData(vo);
        return Result.success(page);
    }


    @OperLog
    @RestResponse
    @Operation(description = "分页查询Mem数据")
    @PostMapping("/page/mem")
    public Result<PageData<MemData>> findPageMemData(@RequestBody CommonVo<DataVo> vo) {
        PageData<MemData> page = memDataService.pageData(vo);
        return Result.success(page);
    }


    @OperLog
    @GetMapping
    @Operation(description = "获取服务器信息")
    public Result<ServerInfo> getInfo(){
        return Result.success(service.getServerInfo());
    }


    @OperLog
    @PostMapping("/save/info")
    @Operation(description = "保存服务器信息")
    public Result<SysData> saveServerInfo(){
        return Result.success(service.saveServerInfo());
    }


    @OperLog
    @PostMapping("/clean/all")
    @Operation(description = "删除全部数据")
    public Result<Boolean> cleanAll(){
        return Result.success(service.cleanAll());
    }



}
