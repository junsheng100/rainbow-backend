package com.rainbow.base.exception;

import com.rainbow.base.constant.HttpCode;

public class ValidException extends BaseException{

  public ValidException(HttpCode httpCode) {
    super(httpCode);
  }

  public ValidException(String module, String code, Object[] args, String defaultMessage) {
    super(module, code, args, defaultMessage);
  }

  public ValidException(String defaultMessage) {
    super(null,null, null, defaultMessage);
  }

}
