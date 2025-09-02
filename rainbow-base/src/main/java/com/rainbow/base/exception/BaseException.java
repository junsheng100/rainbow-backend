package com.rainbow.base.exception;


import com.rainbow.base.constant.HttpCode;
import com.rainbow.base.utils.MessageUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 基础异常
 *
 * @author rainvom
 */
@Data
public class BaseException extends RuntimeException {


  private String module;

  /**
   * 错误码
   */
  private String code;

  /**
   * 错误码对应的参数
   */
  private Object[] args;

  /**
   * 错误消息
   */
  private String defaultMessage;


  protected HttpCode httpCode;

  public BaseException(String module, String code, Object[] args, String defaultMessage) {
    this.module = module;
    this.code = code;
    this.args = args;
    this.defaultMessage = defaultMessage;
  }

  public BaseException(HttpCode httpCode) {
    this.httpCode = httpCode;
    this.code = String.valueOf( httpCode.value());
    this.defaultMessage = httpCode.getReasonPhrase();
  }

  public BaseException(String module, String code, Object[] args) {
    this(module, code, args, null);
  }

  public BaseException(String module, String defaultMessage) {
    this(module, null, null, defaultMessage);
  }

  public BaseException(String code, Object[] args) {
    this(null, code, args, null);
  }

  public BaseException(String defaultMessage) {
    this(null, null, null, defaultMessage);
  }



  @Override
  public String getMessage() {
    String message = null;
    if (!StringUtils.isEmpty(code)) {
      message = MessageUtils.message(code, args);
    }
    if (message == null) {
      message = defaultMessage;
    }
    return message;
  }

  public String getModule() {
    return module;
  }

  public String getCode() {
    return code;
  }

  public Object[] getArgs() {
    return args;
  }

  public String getDefaultMessage() {
    return defaultMessage;
  }

  public HttpCode getHttpCode() {
    return httpCode;
  }
}
