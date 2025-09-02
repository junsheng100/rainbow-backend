package com.rainbow.system.controller;

import com.rainbow.base.controller.BaseController;
import com.rainbow.system.entity.SysIPData;
import com.rainbow.system.service.SysIPDataService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.system.controller
 * @Filename：SysIPAddressController
 * @Date：2025/8/25 09:58
 * @Describe:
 */
@RestController
@RequestMapping("/ipaddr")
@Tag(name = "IP记录")
public class SysIPDataController extends BaseController<SysIPData,String, SysIPDataService> {

}
