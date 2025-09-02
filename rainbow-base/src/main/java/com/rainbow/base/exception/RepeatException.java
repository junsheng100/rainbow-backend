package com.rainbow.base.exception;

public class RepeatException extends BaseException{

  public RepeatException(String message) {
    this(null, null, null, message);
  }

  public RepeatException(String module, String code, Object[] args, String defaultMessage) {
    super(module, code, args, defaultMessage);
  }
}