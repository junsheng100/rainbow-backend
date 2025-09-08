package com.rainbow.base.controller;//package com.rainbow.base.controller;
//
//import com.rainbow.base.model.base.Result;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * API测试控制器
// * 用于验证API前缀配置是否正常工作
// */
//@Api(tags = "API测试")
//@RestController
//@RequestMapping("/test")
//public class ApiTestController {
//
//    /**
//     * 测试API前缀配置
//     * 访问路径: /api/test/prefix
//     */
//    @ApiOperation(value = "测试API前缀", notes = "验证API前缀配置是否正常工作")
//    @GetMapping("/prefix")
//    public Result<String> testApiPrefix() {
//        return Result.success("API前缀配置正常！当前路径: /api/test/prefix");
//    }
//
//    /**
//     * 测试健康检查
//     * 访问路径: /api/test/health
//     */
//    @ApiOperation(value = "健康检查", notes = "简单的健康检查接口")
//    @GetMapping("/health")
//    public Result<String> healthCheck() {
//        return Result.success("服务运行正常！");
//    }
//}
