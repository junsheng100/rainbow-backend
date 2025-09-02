package com.rainbow.base.annotation;


import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchFilter {

  Keyword[] value() default {};

}
