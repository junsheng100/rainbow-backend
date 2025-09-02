package com.rainbow.base.service;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.base.service
 * @Filename：DynamicInvoker
 * @Date：2025/8/16 18:31
 * @Describe:
 */
public interface DynamicInvoker {

    Object invoke(String beanName, String methodName,Object... args);

}
