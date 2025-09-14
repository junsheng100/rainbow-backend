package com.rainbow.aspect;//package com.rainbow.aspect;
//
//import com.rainbow.appdoc.model.InterfaceModel;
//import com.rainbow.appdoc.service.ApiInterfaceService;
//import com.rainbow.base.constant.HttpCode;
//import com.rainbow.base.enums.ClientType;
//import com.rainbow.base.exception.AuthException;
//import com.rainbow.base.utils.StringUtils;
//import com.rainbow.base.utils.UserAgentTools;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.collections.MapUtils;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * @Author：QQ:304299340
// * @Package：com.rainbow.aspect
// * @Filename：AuthAspect
// * @Describe:
// */
//@Aspect
//@Component
//@Order(2)
//@Slf4j
//public class AuthAspect {
//
//  @Autowired
//  private HttpServletRequest request;
//
//  @Autowired
//  private ApiInterfaceService service;
//
//
//  private static final Map<String, Map<String, Boolean>> urlMap = new HashMap<>();
//
//
//  @ResponseBody
//  @Before("execute()")
//  public void validate(JoinPoint joinPoint) throws Throwable {
//
//    ClientType clientType = UserAgentTools.getClientType(request);
//    String url = request.getRequestURI();
//    String method = request.getMethod();
//    log.info(" ##### validate :[{} / {}]", url, clientType);
//
//    List<InterfaceModel> urlList = service.getUrlList();
//    boolean flag =  containsUrl(urlList, url, method, clientType.name());
//
//    if(!flag)
//      throw new AuthException(HttpCode.NOT_FOUND);
//  }
//
//  private boolean containsUrl(List<InterfaceModel> urlList, String url, String method, String clientType) {
//    if (CollectionUtils.isEmpty(urlList)) {
//      urlMap.clear();
//      return false;
//    }
//    if (StringUtils.isBlank(url))
//      return false;
//
//     if(getUrlMap(url,clientType))
//       return true;
//
//
//    List<InterfaceModel> modelList = urlList.stream().filter(item -> isMatchUrl(item.getRequestUrl(), url))
//            .filter(item -> isMatchMethod(item.getRequestMethod(), method))
//            .collect(Collectors.toList());
//
//    if (CollectionUtils.isEmpty(modelList)) {
//      throw new AuthException();
//    }
//    InterfaceModel model = modelList.get(0);
//    if (model.getDisabled() == 1)
//      throw new AuthException(HttpCode.METHOD_NOT_ALLOWED);
//    String clientTypes = model.getClientTypes();
//    if (StringUtils.isNotBlank(clientTypes)) {
//      if (!clientTypes.contains(clientType))
//        throw new AuthException(HttpCode.FORBIDDEN);
//    }
//    Map<String, Boolean> typeMp = new HashMap();
//    typeMp.put(clientType, true);
//    urlMap.put(url, typeMp);
//
//    setUrlMap(url, clientType);
//
//    return true;
//  }
//
//  private boolean getUrlMap(String url, String clientType) {
//    Map<String, Boolean> typeMp = MapUtils.getMap( urlMap,url,null);
//    if(null == typeMp)
//      return false;
//    return typeMp.containsKey(clientType);
//  }
//
//  private void setUrlMap(String url, String clientType) {
//    if (StringUtils.isBlank(url))
//      return;
//    if (StringUtils.isBlank(clientType))
//      return;
//
//    Map<String, Boolean> typeMp = new HashMap();
//    typeMp.put(clientType, true);
//    urlMap.put(url, typeMp);
//
//  }
//
//  private boolean isMatchClientType(String clientTypes, String clientType) {
//    if (StringUtils.isBlank(clientTypes) || StringUtils.isBlank(clientType))
//      return false;
//    if (clientType.equals(ClientType.SERVER.name()))
//      return true;
//    return clientTypes.contains(clientType);
//  }
//
//  private boolean isMatchMethod(String requestMethod, String method) {
//    if (StringUtils.isBlank(requestMethod) || StringUtils.isBlank(method))
//      return false;
//    return requestMethod.equals(method);
//  }
//
//  private boolean isMatchUrl(String requestUrl, String url) {
//
//    if (StringUtils.isBlank(requestUrl) || StringUtils.isBlank(url))
//      return false;
//    if (requestUrl.equals(url))
//      return true;
//    if (requestUrl.indexOf("{") != -1 && requestUrl.indexOf("}") != -1) {
//      String start = StringUtils.substringBefore(requestUrl, "{");
//      String end = StringUtils.substringAfterLast(requestUrl, "}");
//      if (url.startsWith(start) && url.endsWith(end))
//        return true;
//    }
//    return false;
//  }
//
//  @Pointcut("execution(* com.rainbow.**.controller.*.*(..))")
//  public void execute() {
//    log.info("用户登录");
//  }
//}
