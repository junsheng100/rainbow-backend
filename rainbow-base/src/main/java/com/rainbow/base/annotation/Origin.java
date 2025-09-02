package com.rainbow.base.annotation;

import java.lang.annotation.*;

/**
 * @ClassName Source
 * @Description TODO
 * @Author shijunliu
 * @Date 2021/12/19 3:48 下午
 * @Version 1.0
 */
@Documented
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Origin {
	Class SRC(); // 引用类
	String FIELD() ;// 关联
	String JOIN() ;// 本地字段
	String REFER();// 引用类字段
	
}
