package com.rainbow.base.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.base.annotation
 * @Filename：ResultDisplay
 * @Date：2025/9/27 13:03
 * @Describe:
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ResultDisplay {
  String[] value() default  {};
}
