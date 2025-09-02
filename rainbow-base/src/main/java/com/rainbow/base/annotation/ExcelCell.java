package com.rainbow.base.annotation;

import javax.validation.constraints.Size;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author：QQ: 304299340
 * @name：ExcelCell
 * @Filename：ExcelCell
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface ExcelCell {

  @Size(min = 1)
  int value();

  int size() default 1;

  String format() default "";

  String title() default "";

}
