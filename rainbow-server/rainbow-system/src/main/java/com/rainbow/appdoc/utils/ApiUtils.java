package com.rainbow.appdoc.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.rainbow.appdoc.entity.AppCategory;
import com.rainbow.appdoc.entity.AppInterface;
import com.rainbow.appdoc.enums.DataTypeEnums;
import com.rainbow.appdoc.model.ApiRefDataType;
import com.rainbow.appdoc.model.AppData;
import com.rainbow.appdoc.model.AppModel;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.utils.ClassTools;
import com.rainbow.base.utils.Md5Utils;
import com.rainbow.base.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.stream.Collectors;

import static com.rainbow.base.utils.ClassTools.*;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.appInfo.utils
 * @name：ApiUtils
 * @Date：2025/7/22 09:22
 * @Filename：ApiUtils
 */
@Slf4j
public class ApiUtils {

  public final static String KEY_CHILD = "child";


  public static AppData getAppInfo(Class clzz) {
    AppData appData = null;

    AppCategory appCategory = getAppCategory(clzz);
    if (null != appCategory) {
      List<AppInterface> appInterfaces = getAppInterface(clzz);
      appData = new AppData(appCategory, appInterfaces);
    }

    return appData;
  }

  @SneakyThrows
  public static List<AppInterface> getAppInterface(Class clzz) {

    List<AppInterface> list = new ArrayList<>();

    while (clzz != null) {
      for (Method method : clzz.getMethods()) {

        if (!isRequestMethod(method))
          continue;

        AppInterface appInterface = new AppInterface();
        String methodName = method.getName();
        appInterface.setMethodName(methodName);

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
        PutMapping putMapping = method.getAnnotation(PutMapping.class);
        PatchMapping patchMapping = method.getAnnotation(PatchMapping.class);
        /// //////////////////
        Operation operation = method.getAnnotation(Operation.class);

        String requestUrl = null;
        String requestMethod = null;
        String params = null;
        String headers = null;
        String consumes = null;
        String produces = null;
        String description = null;

        if(null != operation){
          description = operation.description();
          if(StringUtils.isBlank(description)){
            description = operation.summary();
          }
        }
        appInterface.setDescription(description);

        if (requestMapping != null) {

          requestUrl = getRequestValue(getMapping.value());
          params = getRequestValue(getMapping.params());
          headers = getRequestValue(getMapping.headers());
          consumes = getRequestValue(getMapping.consumes());
          produces = getRequestValue(getMapping.produces());
          requestMethod = Arrays.asList(requestMapping.method()).stream().map(RequestMethod::name).collect(Collectors.joining(ChartEnum.COMMA.getCode()));
        }
        if (getMapping != null) {
          requestUrl = getRequestValue(getMapping.value());
          params = getRequestValue(getMapping.params());
          headers = getRequestValue(getMapping.headers());
          consumes = getRequestValue(getMapping.consumes());
          produces = getRequestValue(getMapping.produces());
          requestMethod = RequestMethod.GET.name();
        }

        if (postMapping != null) {
          requestUrl = getRequestValue(postMapping.value());
          params = getRequestValue(postMapping.params());
          headers = getRequestValue(postMapping.headers());
          consumes = getRequestValue(postMapping.consumes());
          produces = getRequestValue(postMapping.produces());
          requestMethod = RequestMethod.POST.name();
        }
        if (deleteMapping != null) {
          requestUrl = getRequestValue(deleteMapping.value());
          params = getRequestValue(deleteMapping.params());
          headers = getRequestValue(deleteMapping.headers());
          consumes = getRequestValue(deleteMapping.consumes());
          produces = getRequestValue(deleteMapping.produces());
          requestMethod = RequestMethod.DELETE.name();
        }
        if (putMapping != null) {
          requestUrl = getRequestValue(putMapping.value());
          params = getRequestValue(putMapping.params());
          headers = getRequestValue(putMapping.headers());
          consumes = getRequestValue(putMapping.consumes());
          produces = getRequestValue(putMapping.produces());
          requestMethod = RequestMethod.PUT.name();
        }
        if (patchMapping != null) {
          requestUrl = getRequestValue(patchMapping.value());
          params = getRequestValue(patchMapping.params());
          headers = getRequestValue(patchMapping.headers());
          consumes = getRequestValue(patchMapping.consumes());
          produces = getRequestValue(patchMapping.produces());
          requestMethod = RequestMethod.PATCH.name();
        }
        appInterface.setMethodName(methodName);
        appInterface.setRequestUrl(requestUrl);
        appInterface.setRequestMethod(requestMethod);
        appInterface.setHeaders(headers);
        appInterface.setConsumes(consumes);
        appInterface.setProduces(produces);

        list.add(appInterface);
      }
      clzz = clzz.getSuperclass();
    }

    return list;
  }

  public static List<ApiRefDataType> getAppReturnType(Class<?> clzz) {

    if (null == clzz)
      return null;
    String className = clzz.getName();
    Method[] methods = clzz.getMethods();
    List<ApiRefDataType> list = new ArrayList<>();

    for (Method method : methods) {

      if (isObjectMethod(method))
        continue;
      if (!isPublic(method))
        continue;

      String methodName = method.getName();
      String returnJson = ApiUtils.getReturnJson(method);
      Class<?> returnType = method.getReturnType();
      if (StringUtils.isNotBlank(returnJson)) {
        String md5Code = Md5Utils.getCode(returnJson);
        String typeName = returnType.getName();

        ApiRefDataType type = new ApiRefDataType();

        type.setClassName(className);
        type.setMethodName(methodName);
        type.setMd5Code(md5Code);
        type.setData(returnJson);
        type.setTypeName(typeName);
        type.setDataType(DataTypeEnums.RETURN.getCode());

        list.add(type);
      }

    }
    return list;
  }


  public static List<ApiRefDataType> getAppInputType(Class clzz) {

    if (null == clzz)
      return null;
    String className = clzz.getName();
    Method[] methods = clzz.getMethods();
    List<ApiRefDataType> list = new ArrayList<>();
    for (Method method : methods) {

      if (isObjectMethod(method))
        continue;
      if (!isPublic(method))
        continue;

      String methodName = method.getName();
      Object inputData = ApiUtils.getInputJson(method);
      if (Objects.nonNull(inputData)) {
        if (isCollection(inputData)) {
          List inputList = (List) inputData;
          for (Object item : inputList) {
            ApiRefDataType type = createApiRefDataType(item, className, methodName);
            if (null != type)
              list.add(type);
          }
        } else {
          ApiRefDataType type = createApiRefDataType(inputData, className, methodName);
          if (null != type)
            list.add(type);
        }
      }
    }
    return list;

  }

  private static ApiRefDataType createApiRefDataType(Object inputData, String className, String methodName) {

    if (Objects.nonNull(inputData)) {
      String inputJson = JSON.toJSONString(inputData);
      String md5Code = Md5Utils.getCode(inputJson);

      ApiRefDataType type = new ApiRefDataType();

      type.setClassName(className);
      type.setMethodName(methodName);
      type.setMd5Code(md5Code);
      type.setData(inputJson);
      type.setDataType(DataTypeEnums.PARAMS.getCode());

      return type;
    }
    return null;
  }

  @SneakyThrows
  public static Object getInputJson(Method method) {

    if (isObjectMethod(method))
      throw new BizException("   Can't use Object Method");
    if (!isPublic(method))
      throw new BizException(" Method must Public");


    List list = new ArrayList<>();
    Class<?>[] types = method.getParameterTypes();
    Type[] parameterTypes = method.getGenericParameterTypes();

    if (null == types || types.length == 0)
      return null;


    for (int i = 0; i < parameterTypes.length; i++) {
      Type inputType = parameterTypes[i];
      if (inputType instanceof TypeVariable) {
        getTypeVariable(inputType, list);
      } else if (inputType instanceof Class) {
        getClassType(inputType, list);
      } else if (inputType instanceof ParameterizedType) {
        Map map = getParameterizedType(inputType);
        list.add(map);
      }
    }

    if (types.length == 1) {
      Type type = types[0];
      if (isCollectionType(type)) {
        return list;
      } else {
        return list.get(0);
      }
    }

    return list;
  }


  private static void getTypeVariable(Type inputType, List list) {
    TypeVariable<?> typeVariable = (TypeVariable<?>) inputType;
    String typeName = typeVariable.getName();
    list.add(typeName);
  }

  private static void getClassType(Type type, List list) {
    Boolean isArray = isCollectionType(type);
    Boolean isMap = isMapType(type);

    Class<?> clazz = (Class<?>) type;
    String typeName = clazz.getTypeName();

    if (isArray) {
      Class<?> componentType = clazz.getComponentType();
      if (isWrapperType(componentType)) {
        typeName = componentType.getName();
        list.add(typeName);
      } else {
        Map clzzMp = getClassMap(componentType);
        list.add(clzzMp);
      }
    } else if (isMap) {
      Type[] interfaces = clazz.getGenericInterfaces();
      TypeVariable[] typeVariables = clazz.getTypeParameters();

    } else {
      Object data = getClassData(type);
      list.add(data);
    }


  }

  private static Object getClassData(Type type) {

    Class<?> clazz = (Class<?>) type;
    String typeName = clazz.getTypeName();
    if (isWrapperClass(clazz)) {
      return typeName;
    } else {
      Map clzzMp = getClassMap(clazz);
      return clzzMp;
    }

  }

  @SneakyThrows
  private static Map<String, Object> getParameterizedType(Type inputType) {

    ParameterizedType pt = (ParameterizedType) inputType;
    Type rawType = pt.getRawType();
    String typeName = rawType.getTypeName();

    Class<?> clzz = Class.forName(typeName);

    Map<String, Object> map = getClassMap(clzz);
    Map<String, Object> typeMap = getTypeMap(clzz);
    List<String> typeNames = new ArrayList<>(typeMap.keySet());
    List<AppModel> appModelList = new ArrayList<>();

    Type[] actualTypeArguments = pt.getActualTypeArguments();

    for (int i = 0; i < actualTypeArguments.length; i++) {
      Type type = actualTypeArguments[i];
      String name = typeNames.get(i);
      AppModel model = new AppModel(name, type);
      typeMap.put(name, type);
      analyzeTypeData(model);
      appModelList.add(model);
    }


    for (Map.Entry<String, Object> entry : typeMap.entrySet()) {
      String key = entry.getKey();
      Type value = (Type) entry.getValue();
      List<AppModel> modelList = appModelList.stream().filter(t -> t.getId().equals(key)).collect(Collectors.toList());
      if (CollectionUtils.isNotEmpty(modelList)) {
        Object data = getClassDataType(modelList.get(0), value);
        map.put(key, data);
      }
    }
    return map;

  }

  public static String getReturnJson(Method method) {

    if (isObjectMethod(method))
      throw new BizException("   Can't use Object Method");

    if (!isPublic(method))
      throw new BizException(" Method must Public");

    Class returnClass = method.getReturnType();
    if (ClassTools.isVoid(returnClass))
      return "";
    Boolean isArray = isCollectionClass(returnClass);
    Boolean isMap = isMapClass(returnClass);
    Boolean isWrapper = isWrapperClass(returnClass);


    if (isWrapper)
      return returnClass.getName();

    Map<String, Object> map = getClassMap(returnClass);
    Map<String, Object> typeMap = getTypeMap(returnClass);

    Type returnType = method.getGenericReturnType();

    List<AppModel> appModelList = new ArrayList<>();

    ParameterizedType pt = (ParameterizedType) returnType;
    List<String> typeNames = new ArrayList<>(typeMap.keySet());
    Type[] actualTypeArguments = pt.getActualTypeArguments();

    for (int i = 0; i < actualTypeArguments.length; i++) {
      Type type = actualTypeArguments[i];
      String typeName = type.getTypeName();
      String name = CollectionUtils.isNotEmpty(typeNames) ? typeNames.get(i) : typeName;
      AppModel model = new AppModel(name, type);
      typeMap.put(name, type);
      analyzeTypeData(model);
      appModelList.add(model);

    }


    Object returnData = map;
    if (MapUtils.isNotEmpty(typeMap)) {
      for (Map.Entry<String, Object> entry : typeMap.entrySet()) {
        String key = entry.getKey();
        Type value = (Type) entry.getValue();
        List<AppModel> modelList = appModelList.stream().filter(t -> t.getId().equals(key)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(modelList)) {
          Object data = getClassDataType(modelList.get(0), value);
          if (isArray) {
            List list = new ArrayList();
            list.add(data);
            returnData = list;
          } else if (isWrapper) {
            returnData = data;
          } else {
            map.put(key, data);
            returnData = map;
          }
        }
      }
    }
    return JSON.toJSONString(returnData, JSONWriter.Feature.PrettyFormat);
  }


  private static Object getClassDataType(AppModel appModel, Type type) {
    Boolean isArray = isCollectionType(type);
    Boolean isMap = isMapType(type);
    Boolean isWrapper = isWrapperType(type);
    Boolean isPage = isPage(type);
    Object child = appModel.getChild();


    if (isArray) {
      List mapList = getModelChildList((List<AppModel>) child);
      return mapList;
    } else if (isMap) {
      Map map = new LinkedHashMap();
      getClassModel(map, appModel);
      return map;
    } else if (isWrapper) {
      return child;
    } else if (isPage) {
      List list = getModelChildList((List<AppModel>) child);
      PageImpl<?> page = new PageImpl<>(list, PageRequest.of(0, 10), 0);
      return page;
    } else {
      List<String> classNameList = ClassTools.getClassNameList(type.getTypeName());
      String className = classNameList.get(0);
      Class<?> clzz = ClassTools.forName(className);
      Map map = getClassMap(clzz);
      Map typeMap = ClassTools.getTypeMap(clzz);

      Map dataMap = new LinkedHashMap();

      Object data = getClassModelObject(dataMap, child);
      if (data instanceof Map) {
        if (((Map<?, ?>) data).containsKey(KEY_CHILD)) {

        }

      }
    }
    return null;
  }


  private static List getModelChildList(List<AppModel> child) {
    List<AppModel> modelList = (List<AppModel>) child;
    List mapList = new ArrayList<>();
    for (AppModel model : modelList) {

      if (!isWrapper(child)) {
        Map map = new LinkedHashMap();
        Object data = getClassModel(map, model);

        if (isMap(map) && ((Map<String, Object>) map).containsKey(KEY_CHILD)) {
          Object val = MapUtils.getObject(map, KEY_CHILD);
          mapList.add(val);
        } else {
          mapList.add(data);
        }

      }
    }
    return mapList;
  }


  private static Object getClassModel(Map map, AppModel model) {

    Object child = model.getChild();
    Type type = model.getType();

    if (isCollectionType(type)) {
      return getClassModelList(map, child);
    } else if (isMapType(type)) {
      return getClassModelMap(map, child);
    } else if (isWrapperType(type)) {
      return child;
    } else {
      return getClassModelObject(map, child);
    }

  }


  private static Object getClassModelMap(Map map, Object child) {
    try {
      if (notNull(child)) {
        List<AppModel> resultList = (List<AppModel>) child;

        String key = resultList.get(0).getId();
        Object val = resultList.get(1);

        if (null != val) {
          Boolean isArray = isCollection(val);
          Boolean isMap = isMap(val);

          Map<String, Object> nextMap = new LinkedHashMap<>();
          if (val instanceof AppModel) {
            Object data = getClassModel(nextMap, (AppModel) val);
            if (MapUtils.isNotEmpty(nextMap) && nextMap.containsKey(KEY_CHILD)) {
              map.put(key, nextMap);
            } else {
              map.put(key, data);
            }
          } else if (isArray) {
            getClassModelList(nextMap, val);
            map.put(key, nextMap);
          } else if (isMap) {
            getClassModelMap(nextMap, val);
            map.put(key, nextMap);
          } else {
            getClassModelObject(nextMap, val);
            map.put(key, nextMap);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
    }
    return map;
  }

  private static Object getClassModelObject(Map map, Object child) {
    if (notNull(child)) {
      if (!isWrapper(child)) {
        if (isCollection(child))
          return getClassModelList(map, child);
        Map childMap = JSON.parseObject(JSON.toJSONString(child));
        map.putAll(childMap);
        return map;
      }
    }
    return child;
  }


  private static Object getClassModelList(Map map, Object child) {

    if (child instanceof List) {
      List<AppModel> childList = (List<AppModel>) child;
      List<Map> list = new ArrayList<>();
      for (AppModel next : childList) {
        Map nextMap = new LinkedHashMap();
        getClassModel(nextMap, next);
        list.add(nextMap);
      }
      map.put(KEY_CHILD, list);
    } else {
      map.put(KEY_CHILD, child);
    }
    return map;
  }


  private static void analyzeTypeData(AppModel appModel) {

    Type type = appModel.getType();

    if (type instanceof Class) {
      Class<?> clazz = (Class<?>) type;

      if (isWrapperClass(clazz)) {
        appModel.setChild(clazz.getName());
      } else {
        Map<String, Object> fieldMap = getClassMap(clazz);
        fieldMap = MapUtils.isEmpty(fieldMap) ? new LinkedHashMap<>() : fieldMap;
        appModel.setChild(fieldMap);
      }
    } else if (type instanceof ParameterizedType) {
      ParameterizedType pt = (ParameterizedType) type;
      Type[] args = pt.getActualTypeArguments();
      List<AppModel> child = new ArrayList<>();

      for (int i = 0; i < args.length; i++) {
        Type arg = args[i];
        String name = arg.getTypeName();
        AppModel model = new AppModel(name, arg);
        analyzeTypeData(model);
        child.add(model);
      }
      appModel.setChild(child);
    }


  }


  public static AppCategory getAppCategory(Class clzz) {
    if (null != clzz) {
      RestController restController = (RestController) clzz.getAnnotation(RestController.class);
      Controller controller = (Controller) clzz.getAnnotation(Controller.class);
      RequestMapping requestMapping = (RequestMapping) clzz.getAnnotation(RequestMapping.class);
      if ((restController != null || controller != null) && requestMapping != null) {

        Tag tag = (Tag) clzz.getAnnotation(Tag.class);
        String description = null==tag?"": (StringUtils.isNotBlank(tag.name())? tag.name() : tag.description());

        String className = clzz.getName();
        String simpleName = clzz.getSimpleName();
        String requestUrl = requestMapping.value()[0];

        AppCategory appCategory = new AppCategory();
        appCategory.setClassName(className);
        appCategory.setSimpleName(simpleName);
        appCategory.setRequestUrl(requestUrl);
        appCategory.setDescription(requestMapping.value()[0]);

        appCategory.setDescription(description);

        return appCategory;
      }
    }
    return null;
  }


  public static String getRequestValue(String[] value) {

    if (null != value) {
      return Arrays.asList(value).stream().collect(Collectors.joining(ChartEnum.COMMA.getCode()));
    }
    return null;
  }

  private static boolean isRequestMethod(Method method) {

    return method.isAnnotationPresent(GetMapping.class) ||
            method.isAnnotationPresent(PostMapping.class) ||
            method.isAnnotationPresent(PutMapping.class) ||
            method.isAnnotationPresent(DeleteMapping.class) ||
            method.isAnnotationPresent(PatchMapping.class) ||
            method.isAnnotationPresent(RequestMapping.class);
  }


}
