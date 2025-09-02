package com.rainbow.base.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AnnotationProxyInjector {

    /**
     * 动态添加注解并设置参数值
     *
     * @param field           目标字段
     * @param annotationClass 注解类
     * @param params          注解参数
     */
    @SuppressWarnings("unchecked")
    public static void injectAnnotation(Field field,
                                      Class<? extends Annotation> annotationClass,
                                      Map<String, Object> params) throws Exception {
        // 1. 创建注解代理实例
        InvocationHandler handler = new AnnotationInvocationHandler(annotationClass, params);
        Annotation annotation = (Annotation) Proxy.newProxyInstance(
                annotationClass.getClassLoader(),
                new Class[]{annotationClass},
                handler
        );

        // 2. 获取 Field 类的 declaredAnnotations 字段
        Field declaredAnnotationsField = Field.class.getDeclaredField("declaredAnnotations");
        declaredAnnotationsField.setAccessible(true);

        // 3. 获取或创建注解Map
        Map<Class<? extends Annotation>, Annotation> annotations;
        Object existing = declaredAnnotationsField.get(field);
        
        if (existing == null || existing instanceof Map) {
            annotations = (Map<Class<? extends Annotation>, Annotation>) existing;
            if (annotations == null) {
                annotations = new HashMap<>();
            } else if (annotations.getClass().getName().contains("Immutable")) {
                annotations = new HashMap<>(annotations);
            }
        } else {
            annotations = new HashMap<>();
        }

        // 4. 添加新注解
        annotations.put(annotationClass, annotation);
        
        // 5. 设置回字段
        declaredAnnotationsField.set(field, annotations);
    }

    private static class AnnotationInvocationHandler implements InvocationHandler {
        private final Class<? extends Annotation> annotationType;
        private final Map<String, Object> values;

        public AnnotationInvocationHandler(Class<? extends Annotation> annotationType, Map<String, Object> values) {
            this.annotationType = annotationType;
            this.values = new HashMap<>(values);
            
            // 添加默认值
            for (Method method : annotationType.getDeclaredMethods()) {
                if (!values.containsKey(method.getName())) {
                    Object defaultValue = method.getDefaultValue();
                    if (defaultValue != null) {
                        this.values.put(method.getName(), defaultValue);
                    }
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            
            if (methodName.equals("annotationType")) {
                return annotationType;
            }
            if (methodName.equals("toString")) {
                return annotationType.getName() + values;
            }
            if (methodName.equals("hashCode")) {
                return Objects.hash(annotationType, values);
            }
            if (methodName.equals("equals")) {
                if (args == null || args.length != 1) return false;
                return equals(args[0]);
            }

            return values.getOrDefault(methodName, method.getDefaultValue());
        }

        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!Proxy.isProxyClass(obj.getClass())) return false;
            
            InvocationHandler handler = Proxy.getInvocationHandler(obj);
            if (!(handler instanceof AnnotationInvocationHandler)) return false;
            
            AnnotationInvocationHandler other = (AnnotationInvocationHandler) handler;
            return annotationType.equals(other.annotationType) && values.equals(other.values);
        }
    }

    /**
     * 移除动态注解
     *
     * @param field           目标字段
     * @param annotationClass 要移除的注解类型
     */
    @SuppressWarnings("unchecked")
    public static void removeAnnotation(Field field, Class<? extends Annotation> annotationClass) throws Exception {
        Field declaredAnnotationsField = Field.class.getDeclaredField("declaredAnnotations");
        declaredAnnotationsField.setAccessible(true);

        Map<Class<? extends Annotation>, Annotation> annotations;
        Object existing = declaredAnnotationsField.get(field);
        
        if (existing instanceof Map) {
            annotations = (Map<Class<? extends Annotation>, Annotation>) existing;
            if (annotations.getClass().getName().contains("Immutable")) {
                annotations = new HashMap<>(annotations);
            }
            annotations.remove(annotationClass);
            declaredAnnotationsField.set(field, annotations);
        }
    }
}
