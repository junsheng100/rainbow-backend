package com.rainbow.base.annotation;



import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.base.annotation
 * @Filename：JobTask
 * @Date：2025/8/17 17:38
 * @Describe:
 */

@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JobTask {

  @AliasFor("name")
  String value() default "";

  @AliasFor("value")
  String name() default "";

}
