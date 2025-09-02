package com.rainbow.base.constant;

import java.util.Arrays;
import java.util.List;

public class DataConstant {

  public final static String JWT_HEADER = "Bearer ";
  public final static String JWT_AUTH = "Authorization";
  public final static String JWT_TITLE = "JWT";

  public final static Long MENU_ROOT = 0L;
  public final static Long DEPT_ROOT = 0L;


  public static final String WWW = "www.";
  public static final String HTTP = "http://";
  public static final String HTTPS = "https://";

  /** Layout组件标识 */
  public final static String LAYOUT = "Layout";

  /** ParentView组件标识 */
  public final static String PARENT_VIEW = "ParentView";

  /** InnerLink组件标识 */
  public final static String INNER_LINK = "InnerLink";


  public final static String SERVER_REQUEST_HEADER = "X-Server-Request";
  public final static String SERVER_REQUEST_HEADER_VALUE = "true";

  public static final List<String> EXCLUDE_PATHS = Arrays.asList(
          "/auth/**",
          "/oper/log/receive",
          "/upload/**",
          "/resources/**",
          "/swagger-ui/**",
          "/v3/api-docs/**",
          "/v3/api-docs",
          "/swagger-ui.html",
          "/swagger-resources/**",
          "/webjars/**",
          "/v2/api-docs",
          "/swagger-ui/index.html",
          "/swagger-ui.html/**",
          "/actuator/**",
          "/error",
          "/static/**",
          "/assets/**",
          "/css/**",
          "/js/**",
          "/images/**",
          "/fonts/**",
          "/resources/**",
          "/swagger-ui.html/**",
          "/**/upload",
          "/**/editor/**",
          "/.well-known/**",
          "/.well-known/appspecific/**",
          "/.well-known/appspecific/com.chrome.devtools.json"
  );

  public static final List<String> EXCLUDE_REQUEST = Arrays.asList(
          "/auth",
          "/login/user",
          "/oper/log",
          "/upload/**"

  );

}
