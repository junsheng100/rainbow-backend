package com.rainbow.base.exception;

import com.rainbow.base.constant.HttpCode;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.base.exception
 * @Filename：AuthException
 * @Describe:
 */
public class AuthRoleException extends BaseException {

  public AuthRoleException(String message) {
    this(null, null, null, message);
  }

  public AuthRoleException() {
    this(null, String.valueOf(HttpCode.FORBIDDEN.value()), null, HttpCode.FORBIDDEN.getReasonPhrase());
  }

  public AuthRoleException(HttpCode httpCode) {
    super(httpCode);
  }

  public AuthRoleException(String module, String code, Object[] args, String defaultMessage) {
    super(module, code, args, defaultMessage);
  }
}
