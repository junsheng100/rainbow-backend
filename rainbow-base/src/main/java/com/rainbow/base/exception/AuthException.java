package com.rainbow.base.exception;

import com.rainbow.base.constant.HttpCode;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.base.exception
 * @Filename：AuthException
 * @Describe:
 */
public class AuthException extends BaseException {

  public AuthException(String message) {
    this(null, null, null, message);
  }

  public AuthException() {
    this(null, String.valueOf(HttpCode.FORBIDDEN.value()), null, HttpCode.FORBIDDEN.getReasonPhrase());
  }

  public AuthException(HttpCode httpCode) {
    super(httpCode);
  }

  public AuthException(String module, String code, Object[] args, String defaultMessage) {
    super(module, code, args, defaultMessage);
  }
}
