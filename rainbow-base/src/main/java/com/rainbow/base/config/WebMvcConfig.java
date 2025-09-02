package com.rainbow.base.config;

import com.rainbow.base.interceptor.NoRepeatSubmitInterceptor;
import com.rainbow.base.interceptor.XssInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.rainbow.base.constant.DataConstant.EXCLUDE_PATHS;

/**
 * Web MVC 配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Autowired
  private NoRepeatSubmitInterceptor noRepeatSubmitInterceptor;

  @Autowired
  private XssInterceptor xssInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 注册XSS拦截器（在其他拦截器之前）
    registry.addInterceptor(xssInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(EXCLUDE_PATHS);

    // 注册防重复提交拦截器
    registry.addInterceptor(noRepeatSubmitInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(EXCLUDE_PATHS
            );
  }

  // 在控制器或配置类中添加


  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("*")
            .allowedHeaders("*");
  }


}
