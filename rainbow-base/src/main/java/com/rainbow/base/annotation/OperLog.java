package com.rainbow.base.annotation;


import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface OperLog {

  @AliasFor("title")
  String value() default "";

  @AliasFor("value")
  String title() default "";

}
