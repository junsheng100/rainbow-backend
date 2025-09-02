package com.rainbow.base.config;

import com.rainbow.base.filter.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

/**
 * XSS过滤器配置
 */
@Configuration
public class XssFilterConfig {

    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistration() {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
        // 注册XSS过滤器
        registration.setFilter(new XssFilter());
        // 设置过滤器名称
        registration.setName("xssFilter");
        // 设置过滤路径
        registration.addUrlPatterns("/*");
        // 设置过滤器优先级，值越小优先级越高
        registration.setOrder(1);
        // 设置过滤器的转发类型
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        return registration;
    }
}
