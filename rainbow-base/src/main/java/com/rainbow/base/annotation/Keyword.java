package com.rainbow.base.annotation;


import com.rainbow.base.enums.SearchEnum;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD,ElementType.FIELD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Keyword   {

   String key() default "";
   String value() default "";
   SearchEnum SELECT() default SearchEnum.EQ;

}
