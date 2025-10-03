package com.rainbow.base.annotation;

import org.springframework.core.annotation.Order;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName MyCache
 * @Description TODO
 * @Author QQ:304299340 
 * @Version 1.0
 */
@Order(999)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CacheRead {

    String NAME() default "";

    long TIME() default 15L;

    boolean CLEAN() default false;

    TimeUnit TIME_UNIT() default TimeUnit.MINUTES;

}
