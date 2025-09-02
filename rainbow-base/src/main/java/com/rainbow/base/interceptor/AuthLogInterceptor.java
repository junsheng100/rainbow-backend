package com.rainbow.base.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class AuthLogInterceptor
implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthLogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      // 记录请求开始时间
      request.setAttribute("requestStartTime", System.currentTimeMillis());

      logger.info("======= Request Start =======");
      logger.info("Request URL: {}", request.getRequestURL().toString());
      logger.info("HTTP Method: {}", request.getMethod());
      logger.info("IP Address: {}", request.getRemoteAddr());
      logger.info("Request Headers:");

      Enumeration<String> headerNames = request.getHeaderNames();
      while (headerNames.hasMoreElements()) {
        String headerName = headerNames.nextElement();
        logger.info("  {}: {}", headerName, request.getHeader(headerName));
      }

      logger.info("Request Parameters:");
      Enumeration<String> paramNames = request.getParameterNames();
      while (paramNames.hasMoreElements()) {
        String paramName = paramNames.nextElement();
        logger.info("  {}: {}", paramName, request.getParameter(paramName));
      }

      return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
      // 可以在这里记录响应信息
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
      // 计算请求处理时间
      long startTime = (Long) request.getAttribute("requestStartTime");
      long endTime = System.currentTimeMillis();
      long executeTime = endTime - startTime;

      logger.info("Response Status: {}", response.getStatus());
      logger.info("Request Execution Time: {}ms", executeTime);
      logger.info("======= Request End =======");
    }
}
