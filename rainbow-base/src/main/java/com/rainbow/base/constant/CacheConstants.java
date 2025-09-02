package com.rainbow.base.constant;

import io.jsonwebtoken.Claims;

/**
 * 缓存的key 常量
 *
 * @author rainvom
 */
public class CacheConstants {


  public static final String TOKEN_KEY_PREFIX = "jwt:token:";
  public static final String REFRESH_TOKEN_KEY_PREFIX = "jwt:refresh:";

  /**  用户ID */
  public static final String JWT_USERID = "jwt:user:";
  public static final String JWT_USER_TYPE = "userType";
  public static final String JWT_LOGIN_TIME = "loginTime";
  /** 用户名称  */
  public static final String JWT_USERNAME = Claims.SUBJECT;

  public final static String CACHE_KEY_URL = "AppInterface:urlList";

  /**
   * 验证码 redis key
   */
  public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

  /**
   * 参数管理 cache key
   */
  public static final String SYS_CONFIG_KEY = "sys_config:";

  /**
   * 字典管理 cache key
   */
  public static final String SYS_DICT_KEY = "sys_dict:";

  /**
   * 防重提交 redis key
   */
  public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

  /**
   * 限流 redis key
   */
  public static final String RATE_LIMIT_KEY = "rate_limit:";

  /**
   * 登录账户密码错误次数 redis key
   */
  public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";
}
