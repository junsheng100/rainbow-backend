package com.rainbow.base.interceptor;

import com.rainbow.base.constant.HttpCode;
import com.rainbow.base.exception.*;
import com.rainbow.base.model.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {


  @ExceptionHandler({
          Exception.class,
          BaseException.class,
          BizException.class,
          ValidException.class,
          DataException.class,
          NoLoginException.class,
          AuthenticationException.class,
          AuthException.class,
          AccessDeniedException.class,
          ResponseStatusException.class,
          RepeatException.class,
          RuntimeException.class})
  public ResponseEntity<Object> handleException(RuntimeException e) {
    log.error(e.getClass().getSimpleName());

    String message = e.getMessage();

    HttpCode httpCode = null;

    if (e instanceof AuthException   ) {
      httpCode = HttpCode.FORBIDDEN;
      message = StringUtils.isBlank(message) ? httpCode.getReasonPhrase() : message;
    } else if (e instanceof NoLoginException) {
      httpCode = HttpCode.UNAUTHORIZED;
      message = StringUtils.isBlank(message) ? "token过期或失效" : message;
    } else if (e instanceof RepeatException) {
      httpCode = HttpCode.TOO_MANY_REQUESTS;
      message = StringUtils.isBlank(message) ? httpCode.getReasonPhrase() : message;
    } else if (e instanceof BizException) {
      httpCode = HttpCode.INTERNAL_BIZ_ERROR;
      message = StringUtils.isBlank(message) ? httpCode.getReasonPhrase() : message;
    } else if (e instanceof DataException) {
      httpCode = HttpCode.INTERNAL_DATA_ERROR;
      message = StringUtils.isBlank(message) ? httpCode.getReasonPhrase() : message;
    } else if (e instanceof ResponseStatusException) {
      httpCode = HttpCode.BAD_REQUEST;
      message = StringUtils.isBlank(message) ? httpCode.getReasonPhrase() : message;
    } else if (e instanceof BaseException) {
      httpCode = HttpCode.INTERNAL_DATA_ERROR;
      message = StringUtils.isBlank(message) ? httpCode.getReasonPhrase() : message;
    } else {
      httpCode = HttpCode.INTERNAL_BIZ_ERROR;
    }

    int code = httpCode.value();

    if (isResponseStatus(httpCode)) {
      message = StringUtils.isBlank(message) ? "" : message;
      return ResponseEntity
              .status(code)
              .body(Result.error(code, message));
    } else {
      message = StringUtils.isBlank(message) ? httpCode.getReasonPhrase() : message;
      return ResponseEntity
              .status(HttpCode.OK.value())
              .body(Result.error(code, message));
    }
  }

  private boolean isResponseStatus(HttpCode httpCode) {

    boolean flag = false;
    switch (httpCode) {
      case UNAUTHORIZED:
        flag = true;
        break;
      case BAD_REQUEST:
        flag = true;
        break;
      case FORBIDDEN:
        flag = true;
        break;
    }
    return flag;
  }


}