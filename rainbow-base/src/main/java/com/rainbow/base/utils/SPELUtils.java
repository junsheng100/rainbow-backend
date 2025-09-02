package com.rainbow.base.utils;

import com.alibaba.fastjson2.JSON;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.exception.DataException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @Author：QQ: 304299340
 * @Package：com.rainbow.base.utils
 * @name：SPELUtils
 * @Filename：SPELUtils
 */
@Slf4j
public class SPELUtils {

  private static final ExpressionParser parser = new SpelExpressionParser();


  public static StandardEvaluationContext getStandardEvaluationContext(Object data) {

    if (Objects.isNull(data))
      throw new NullPointerException("data is null");

    StandardEvaluationContext context = new StandardEvaluationContext(data);


    List<Field> fieldList = Arrays.asList(data.getClass().getDeclaredFields());
    if (CollectionUtils.isEmpty(fieldList))
      throw new DataException("data is null");

    for (Field field : fieldList) {
      field.setAccessible(true);
      String name = field.getName();
      Object val = getVal(field, data);
      if (null != val)
        context.setVariable(name, val);
    }

    return context;
  }

  public static Object getData(Object data) {
    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    List<Field> fieldList = Arrays.asList(data.getClass().getDeclaredFields());
    if (CollectionUtils.isEmpty(fieldList))
      throw new DataException("data is null");

    for (Field field : fieldList) {
      field.setAccessible(true);
      String name = field.getName();
      Object val = getVal(field, data);
      if (null != val) {
        map.put(name, val);
      }

    }
    return map;
  }

  public static Object getVal(Field field, Object data) {
    if (Objects.isNull(data))
      return null;
    if (null == field)
      return null;
    field.setAccessible(true);
    Object val = null;
    try {
      val = field.get(data);
      if (val == null)
        return null;
      if (val instanceof String) {
        if (StringUtils.isBlank(((String) val).trim()))
          return null;
      }
    } catch (IllegalAccessException e) {
      return null;
    }
    return val;
  }


  public static String getContext(String srcContent, Map<String, Object> dataMap,Map<String, Object> variable) {
    String content = null;
    Object instance = null;
    CtClass ctClass = null;
    try {
      if (MapUtils.isEmpty(dataMap))
        return null;

      ClassPool pool = ClassPool.getDefault();
      String pkgName = SPELUtils.class.getPackage().getName();
      String clzName = pkgName +".Dy" + RandomId.generateShortUuid();
      // 1. 创建新类
      ctClass = pool.makeClass(clzName);


      for (Map.Entry<String, Object> entry : dataMap.entrySet()) {

        // 2. 添加字段
        String key = entry.getKey();
        Object value = entry.getValue();

        CtField field = new CtField(pool.get(value.getClass().getName()), key, ctClass);
        field.setModifiers(Modifier.PRIVATE);

        ctClass.addField(field);

        ctClass.addMethod(CtNewMethod.make("public void set" + StringUtils.capitalize(key) + "(" + value.getClass().getName() + " " + key + ") {this." + key + " = " + key + ";}", ctClass));
        ctClass.addMethod(CtNewMethod.make("public " + value.getClass().getName() + " get" + StringUtils.capitalize(key) + "() {return " + key + ";}", ctClass));

      }

      // 4. 生成类并实例化
      Class<?> clazz = ctClass.toClass();
      String json = JSON.toJSONString(dataMap);
      instance = JSON.parseObject(json, clazz);

      ParserContext ctx = new TemplateParserContext();
      StandardEvaluationContext evaluationContext = getStandardEvaluationContext(instance);

      if( MapUtils.isNotEmpty(variable)) {
        for (Map.Entry<String, Object> entry : variable.entrySet()) {
          String key = entry.getKey();
          Object value = entry.getValue();
          evaluationContext.setVariable(key,value);
        }
      }

      Expression exp = parser.parseExpression(srcContent, ctx);

      if (null == evaluationContext)
        throw new BizException("模板配置错误");
      content = exp.getValue(evaluationContext, String.class);

    } catch (Exception e) {
      log.debug(e.getMessage());
      throw new BizException(e.getMessage());
    }
    return content;
  }

}
