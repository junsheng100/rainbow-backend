package com.rainbow.system.controller;

import com.rainbow.base.controller.BaseController;
import com.rainbow.system.entity.SysConfig;
import com.rainbow.system.service.SysConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
@Tag(name = "系统设置")
public class SysConfigController extends BaseController<SysConfig,Long,SysConfigService> {
}
