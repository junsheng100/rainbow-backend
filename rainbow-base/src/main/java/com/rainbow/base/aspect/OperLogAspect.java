package com.rainbow.base.aspect;


import com.alibaba.fastjson2.JSON;
import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.client.OperLogClient;
import com.rainbow.base.client.UserClient;
import com.rainbow.base.constant.DataConstant;
import com.rainbow.base.exception.BaseException;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.vo.OperLogVo;
import com.rainbow.base.utils.AddressUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Aspect
@Component
@Order(1)
@Slf4j
public class OperLogAspect {

  @Autowired
  private HttpServletRequest request;
  @Autowired
  private UserClient userClient;
  @Autowired
  private OperLogClient logClient;


  @ResponseBody
  @Around("execute()")
  public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    OperLogVo vo = null;
    Object data = null;
    try {
      vo = getOperLogVo(joinPoint);
        data = joinPoint.proceed();
      if (null != vo) {
        String json = null != data ? JSON.toJSONString(data) : "";
        vo.setJsonResult("数据长度:"+json.length());
      }
    } catch (BaseException e) {
      log.debug(e.getMessage());
      if (null != vo) {
        vo.setErrorMsg(e.getMessage());
        vo.setErrCode(e.getCode());
        sendLogMess(vo);

        return Result.error(e.getCode(), e.getMessage());
      }
    } finally {
      if (null != vo) {
        Long time = System.currentTimeMillis() - vo.getOperTime().getTime();
        vo.setCostTime(time);
        sendLogMess(vo);
      }
    }
    return data;
  }

  private void sendLogMess(OperLogVo vo) {
    logClient.sendLogMess(vo);
  }


  private OperLogVo getOperLogVo(ProceedingJoinPoint joinPoint) {
    OperLogVo vo = null;
    try {
      MethodSignature signature = (MethodSignature) joinPoint.getSignature();
      // 获取类名（简化的，不带包名）
      Class clzz = joinPoint.getTarget().getClass();

      if (clzz.getSimpleName().startsWith("SysOperLog"))
        return null;

      RequestMapping requestMapping = (RequestMapping) clzz.getAnnotation(RequestMapping.class);
      String[] paths = requestMapping.value();

      if (null == paths)
        return null;

      if (isExcludeRequest(paths))
        return null;
      Method method = signature.getMethod();
      Tag tag = (Tag) clzz.getAnnotation(Tag.class);
      OperLog operLog = method.getAnnotation(OperLog.class);
      Operation operation = method.getAnnotation(Operation.class);

      String className = clzz.getSimpleName();
      String methodName = method.getName();


      /// /////////////////////////
      String tagTitle = tag.name();
      tagTitle = StringUtils.isBlank(tagTitle)?tag.description():tagTitle;
      tagTitle = StringUtils.isBlank(tagTitle)?className:tagTitle;
      /// ////////////////////////
      String summary =  operation.summary();
      summary = StringUtils.isBlank(summary) ? operation.description() : summary;
      summary = StringUtils.isBlank(summary) ? operLog.value() : summary;
      summary = StringUtils.isBlank(summary) ? methodName : summary;

      String title = tagTitle + ":" + summary;

      // 获取方法名

      String[] parameterNames = signature.getParameterNames();
      Object[] parameterValues = joinPoint.getArgs();
      String ip = AddressUtils.getIpAddress();
      UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
      // 获取客户端操作系统
      String os = userAgent.getOperatingSystem().getName();
      // 获取客户端浏览器
      String browser = userAgent.getBrowser().getName();
      String url = request.getRequestURI();
      url = url.length() > 255 ? url.substring(0, 255) : url;


      String operName = userClient.getUserName();
      String requestMethod = request.getMethod();
      String operParam = JSON.toJSONString(parameterValues);

      vo = new OperLogVo();
      vo.setTitle(title);
      vo.setOperIp(ip);
      vo.setOperUrl(url);
      vo.setMethod(methodName);
      vo.setOperatorType(os);
      vo.setOperName(operName);
      vo.setBrowser(browser);
      vo.setRequestMethod(requestMethod);
      vo.setOperTime(new Date());
      vo.setOperParam(operParam);
      vo.setBeanName(className);

    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
    }
    return vo;
  }

  private boolean isExcludeRequest(String[] paths) {

    List<String> pathList = Arrays.asList(paths);
    Long count = DataConstant.EXCLUDE_REQUEST.stream().filter(t -> pathList.contains(t)).count();
    return count > 0L;

  }


  @Pointcut("@annotation(com.rainbow.base.annotation.OperLog)")
  public void execute() {

  }


}
