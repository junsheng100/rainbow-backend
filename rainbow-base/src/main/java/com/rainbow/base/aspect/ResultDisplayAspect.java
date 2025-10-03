package com.rainbow.base.aspect;

import cn.hutool.db.Page;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rainbow.base.annotation.ResultDisplay;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.exception.BaseException;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.utils.ClassTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.base.aspect
 * @Filename：ResultDisplayAspect
 * @Date：2025/9/27 13:09
 * @Describe:
 */
@Aspect
@Component
@Order(Integer.MAX_VALUE - 1)
@Slf4j
public class ResultDisplayAspect {

  @ResponseBody
  @Around("execute()")
  public Object around(ProceedingJoinPoint pjp) throws Throwable {

    try {
      Object data = pjp.proceed();
      data = showResult(pjp, data);
      return data;
    } catch (BaseException e) {
      e.printStackTrace();
      log.error(e.getMessage());
      return Result.error(e.getCode(), e.getMessage());
    }

  }

  private Object showResult(ProceedingJoinPoint pjp, Object response) {
    if (null == response)
      return response;
    Signature signature = pjp.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    ResultDisplay display = methodSignature.getMethod().getAnnotation(ResultDisplay.class);

    if (null == display)
      return response;

    String[] value = display.value();
    if (null == value || value.length == 0)
      return response;

    List<String> keyList = getKeyList(display);

    if (response instanceof Result) {
      Object res = ((Result<?>) response).getData();
      if (validCollection(res)) {
        res = convertCollection(res, keyList);
      } else if (res instanceof Object) {
        res = convertObject(res, display);
      }
      response = Result.success(res);
      return response;
    } else if (validPageData(response)) {
      PageData pageData = convertPageData(response);
      return pageData;
    } else if (ClassTools.isCollection(response)) {
      List<JSONObject> list = new ArrayList<>();
      for (Object one : (Collection) response) {
        JSONObject result = getObject(one, keyList);
        list.add(result);
      }
      return list;
    } else if (ClassTools.isWrapper(response)) {
      return response;
    } else {
      return getObject(response, keyList);
    }

  }


  private PageData convertPageData(Object res) {
    PageData pageData = null;

    if (res instanceof PageData)
      pageData = (PageData) res;
    if (res instanceof Page)
      pageData = new PageData((org.springframework.data.domain.Page) res);
    if (res instanceof PageImpl)
      pageData = new PageData((org.springframework.data.domain.PageImpl) res);

    Object resData = pageData.getContent();
    List<JSONObject> list = new ArrayList<>();
    for (Object one : (Collection) resData) {
      JSONObject result = getObject(one, null);
      list.add(result);
    }
    pageData.setContent(list);
    return pageData;
  }

  private boolean validPageData(Object res) {
    if (res instanceof PageData)
      return true;
    if (res instanceof Page)
      return true;
    if (res instanceof PageImpl)
      return true;
    return false;
  }


  private Object convertCollection(Object res, List<String> keyList) {

    if (res instanceof Collection) {
      List<JSONObject> list = new ArrayList<>();
      for (Object one : (Collection) res) {
        JSONObject result = getObject(one, keyList);
        list.add(result);
      }
      return list;
    } else if (validPageData(res)) {
      return convertPageData(res);
    }
    return res;
  }

  private JSONObject convertObject(Object res, ResultDisplay display) {

    List<String> keyList = getKeyList(display);

    JSONObject result = getObject(res, keyList);
    return result;
  }

  private JSONObject getObject(Object res, List<String> keyList) {
    String json = JSON.toJSONString(res);
    JSONObject jsonObject = JSON.parseObject(json);
    JSONObject result = new JSONObject();
    if (CollectionUtils.isNotEmpty(keyList)) {
      for (String key : keyList) {
        result.put(key, jsonObject.get(key));
      }
    }
    return result;
  }

  private List<String> getKeyList(ResultDisplay display) {
    String[] keys = display.value();
    if (null == keys || keys.length == 0)
      return null;
    List<String> keyList = new ArrayList<>();
    for (String key : keys) {
      String[] akeys = key.split(ChartEnum.COMMA.getCode());
      keyList.addAll(Arrays.asList(akeys));
    }
    return keyList;
  }

  private boolean validCollection(Object res) {
    if (res instanceof Collection)
      return true;
    if (res instanceof PageData)
      return true;
    if (res instanceof Page || res instanceof PageImpl)
      return true;

    return false;
  }

  @Pointcut("@annotation(com.rainbow.base.annotation.ResultDisplay)")
  public void execute() {

  }

}
