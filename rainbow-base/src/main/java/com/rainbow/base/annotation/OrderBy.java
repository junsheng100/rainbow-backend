package com.rainbow.base.annotation;

import org.springframework.data.domain.Sort;

import java.lang.annotation.*;

/**
 * @ClassName OrderBy
 * @Description TODO
 * @Author shijunliu
 * @Date 2024/12/29 9:47 下午
 * @Version 1.0
 */
@Documented
@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderBy {
	Sort.Direction value() default Sort.Direction.ASC;
	String INDEX() default  "1";
}
