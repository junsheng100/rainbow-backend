package com.rainbow.base.service.impl;

import com.rainbow.base.service.DynamicInvoker;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.base.service.impl
 * @Filename：DynamicInvokerImpl
 * @Describe:
 */
@Component
public class DynamicInvokerImpl implements DynamicInvoker {


  @Autowired
  private ApplicationContext ctx;
  private final Map<String, Method> methodCache = new ConcurrentHashMap<>();


  @SneakyThrows
  public Object invoke(String beanName, String methodName, Object... args) {
    try {
      Object bean = ctx.getBean(beanName);
      Class<?>[] paramTypes = getParamTypes(args);

      String cacheKey = generateCacheKey(beanName, methodName, paramTypes);
      Method method = methodCache.computeIfAbsent(cacheKey,
              k -> {
                try {
                  return findMethod(bean, methodName, paramTypes);
                } catch (NoSuchMethodException e) {
                  throw new RuntimeException(e);
                }
              });


      return method.invoke(bean, args);
    } catch (InvocationTargetException e) {
      throw (Exception) e.getTargetException();
    }
  }

  private Class<?>[] getParamTypes(Object... args) {
    return Arrays.stream(args)
            .map(Object::getClass)
            .toArray(Class<?>[]::new);
  }

  private Method findMethod(Object bean, String methodName, Class<?>[] paramTypes) throws NoSuchMethodException {
    return Arrays.stream(bean.getClass().getMethods())
            .filter(m -> m.getName().equals(methodName))
            .filter(m -> Arrays.equals(m.getParameterTypes(), paramTypes))
            .findFirst()
            .orElseThrow(() -> new NoSuchMethodException(
                    "Method not found: " + methodName + " with specified parameters"));
  }

  private String generateCacheKey(String beanName, String methodName, Class<?>[] paramTypes) {
    return beanName + "::" + methodName + "::" +
            Arrays.stream(paramTypes)
                    .map(Class::getName)
                    .collect(Collectors.joining(","));
  }

}

