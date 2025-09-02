package com.rainbow.base.interceptor;

import com.rainbow.base.annotation.NoRepeatSubmit;
import com.rainbow.base.exception.RepeatException;
import com.rainbow.base.service.RedisService;
import com.rainbow.base.utils.Md5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 防重复提交拦截器
 */
@Component
public class NoRepeatSubmitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        NoRepeatSubmit annotation = method.getAnnotation(NoRepeatSubmit.class);

        if (annotation == null) {
            return true;
        }

        // 获取用户标识（可以是token、session等）
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            token = request.getSession().getId();
        }

        // 获取请求特征，构建唯一标识
        Map<String, Object> requestFeature = new HashMap<>();
        requestFeature.put("uri", request.getRequestURI());
        requestFeature.put("method", request.getMethod());
        requestFeature.put("token", token);
        String requestParams = request.getParameterMap().toString();
        requestFeature.put("params", requestParams);

        // 生成请求唯一标识
        String requestId = Md5Utils.getCode(requestFeature.toString());
        String redisKey = "repeat_submit:" + requestId;

        // 检查是否存在重复提交标记
        if (redisService.hasKey(redisKey)) {
            throw new RepeatException(annotation.message());
        }

        // 设置重复提交标记
        redisService.setCacheObject(redisKey, requestId, annotation.interval(), annotation.timeUnit());
        return true;
    }
}
