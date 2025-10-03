package com.rainbow.base.annotation;

import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * @ClassName MyCache
 * @Description TODO
 * @Author QQ:304299340
 * @Version 1.0
 */
@Order(998)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CacheClean {

    String[] NAME() default "";

}
