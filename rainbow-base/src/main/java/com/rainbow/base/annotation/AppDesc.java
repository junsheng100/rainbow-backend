package com.rainbow.base.annotation;

import java.lang.annotation.*;

/**
 * @Author：QQ: 304299340
 * @Package：com.rainbow.base.api
 * @Filename：ApiDescribe 
 * @Describe: 用于描述[类,属性,方法,参数]的说明
 */
@Documented
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppDesc {

  String value() default "";

  String description() default "";

}
