package com.rainbow.aspect;

import com.alibaba.fastjson2.JSON;
import com.rainbow.base.annotation.CacheRead;
import com.rainbow.base.service.RedisService;
import com.rainbow.base.utils.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName MyCacheAspect
 * @Description TODO
 * @Author QQ:304299340
 * @Date 2021/1/27 1:57 上午
 * @Version 1.0
 */
@Slf4j
@Aspect
@Order(998)
@Component
public class CacheReadAspect {

    @Autowired
    private RedisService redisService;

    @Pointcut("@annotation(com.rainbow.base.annotation.CacheRead)")
    public void execute() {
    }


    @ResponseBody
    @Around("execute()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        try {
            Signature signature = pjp.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            CacheRead cacheRead = methodSignature.getMethod().getAnnotation(CacheRead.class);
            String key = readCacheKey(pjp, cacheRead);

            if(!cacheRead.CLEAN())
                result = getResult(key);

            if (null == result) {
                result = pjp.proceed();
                writeCache(key, result, cacheRead);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return result;
    }

    private void writeCache(String key, Object result, CacheRead cacheRead) {
        try {
            if (StringUtils.isBlank(key)) return;
            if (null == result) return;
            if (cacheRead.CLEAN()) {
                redisService.deleteObject(key);
            } else {
                int TIME = Long.valueOf(cacheRead.TIME()).intValue();
                TimeUnit TIME_UNIT = cacheRead.TIME_UNIT();
                redisService.setCacheObject(key, result, TIME, TIME_UNIT);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private Object getResult(String key) {
        if (StringUtils.isNotBlank(key)) {
            Object val = redisService.getCacheObject(key);
            return val;
        }
        return null;
    }

    private String readCacheKey(ProceedingJoinPoint pjp, CacheRead cacheRead) {
        String key = "";
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String keyName = cacheRead.NAME();

        Method method = methodSignature.getMethod();
        Object[] args = pjp.getArgs();


        if (StringUtils.isNotBlank(keyName)) {
            key = keyName;
        } else {
            String clzzName = method.getDeclaringClass().getSimpleName();
            String monthName = method.getName();
            key =  clzzName + ":" + monthName;
        }

        if (null != args) {
            String json = JSON.toJSONString(args);
            String md5Code = Md5Utils.getCode(json);
            key += ":" + md5Code;
        }
        return key;
    }
}
