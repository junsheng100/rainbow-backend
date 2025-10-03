package com.rainbow.aspect;

import com.rainbow.base.annotation.CacheClean;
import com.rainbow.base.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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

import java.util.*;

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
public class CacheCleanAspect {

    @Autowired
    private RedisService redisService;

    @Pointcut("@annotation(com.rainbow.base.annotation.CacheClean)")
    public void execute() {
    }


    @ResponseBody
    @Around("execute()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        try {
            result = pjp.proceed();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }finally {
            CacheClean myCache = methodSignature.getMethod().getAnnotation(CacheClean.class);
            String[] key = myCache.NAME();
            if(null != key && key.length>0) {
                List keys = Arrays.asList(key);
                List<String> keyList = new ArrayList(new HashSet(keys));
                for(String akey:keyList){
                    Set<String> allKeys = redisService.redisTemplate.keys(akey+"*");
                    if(CollectionUtils.isNotEmpty(allKeys)){
                        for(String oneKey:allKeys){
                            redisService.redisTemplate.delete(oneKey);
                        }
                    }
                }
            }
        }
        return result;
    }


}
