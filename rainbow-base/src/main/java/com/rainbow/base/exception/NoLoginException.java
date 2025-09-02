package com.rainbow.base.exception;

public class NoLoginException extends BaseException {



  public NoLoginException(String message) {
    this(null, null, null, message);
  }

  public NoLoginException(String module, String code, Object[] args, String defaultMessage) {
    super(module, code, args, defaultMessage);
  }

}
