package com.rainbow.base.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * API路径配置
 * 为所有请求添加统一的API前缀
 */
@Configuration
public class ApiPathConfig implements WebMvcConfigurer {

    /**
     * 配置路径匹配
     * 确保所有控制器都能正确处理带前缀的请求
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 启用后缀模式匹配，支持.html等后缀
        configurer.setUseSuffixPatternMatch(false)
                // 启用尾部斜杠匹配
                .setUseTrailingSlashMatch(true);
    }
}
