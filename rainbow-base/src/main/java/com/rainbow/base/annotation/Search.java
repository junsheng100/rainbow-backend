package com.rainbow.base.annotation;


import com.rainbow.base.enums.SearchEnum;

import java.lang.annotation.*;

/**
 * @ClassName Search
 * @Description TODO
 * @Author shijunliu
 * @Date 2021/7/13 9:08 上午
 * @Version 1.0
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Search {

	SearchEnum SELECT() default SearchEnum.EQ;
	String COLUMN() default "";
	
}
