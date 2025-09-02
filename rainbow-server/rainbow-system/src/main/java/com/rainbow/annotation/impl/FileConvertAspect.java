package com.rainbow.annotation.impl;

import com.rainbow.base.exception.BaseException;
import com.rainbow.base.model.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(Integer.MAX_VALUE)
@Slf4j
public class FileConvertAspect {


  @Around("execute()")
  public Object around(ProceedingJoinPoint pjp) throws Throwable {

    try {
      Object data = pjp.proceed();
      return data;
    } catch (BaseException e) {
      e.printStackTrace();
      log.error(e.getMessage());
      return Result.error(e.getCode(),e.getMessage());
    }

  }


  @Pointcut("@annotation(com.rainbow.annotation.FileConvert)")
  public void execute() {

  }


}
