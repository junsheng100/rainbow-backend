package com.rainbow.base.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.model.bean.FieldModel;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author：QQ: 304299340
 * @Package：com.rainbow.base.utils
 * @name：FieldUtils
 * @Filename：FieldUtils
 */
public class ClassTools {

  // 基础类型映射
  public static final Map<String, String> PRIMITIVE_TYPE_MAP = new LinkedHashMap<>();

  public static final String VOID = "void";
  public static final String KEY_PAGE = "org.springframework.data.domain.Page";
  public static final String KEY_LIST = "java.util.List";
  public static final String KEY_MAP = "java.util.Map";
  public static final String TAG_START = "<";
  public static final String TAG_END = ">";

  static {
    PRIMITIVE_TYPE_MAP.put("Z", "boolean");
    PRIMITIVE_TYPE_MAP.put("C", "char");
    PRIMITIVE_TYPE_MAP.put("B", "byte");
    PRIMITIVE_TYPE_MAP.put("S", "short");
    PRIMITIVE_TYPE_MAP.put("I", "int");
    PRIMITIVE_TYPE_MAP.put("J", "long");
    PRIMITIVE_TYPE_MAP.put("F", "float");
    PRIMITIVE_TYPE_MAP.put("D", "double");
    PRIMITIVE_TYPE_MAP.put("V", "void");
  }

  public static boolean applyMethod(Method method) {
    if (null == method)
      return false;

    return isRequestMethod(method) &&
            !isObjectMethod(method) &&
            !isStaticMethod(method) &&
            !isPrivate(method) &&
            !isProtected(method) &&
            !method.isSynthetic() &&
            !method.isBridge() &&
            !method.isAnnotationPresent(JsonIgnore.class) &&
            !method.isAnnotationPresent(JsonIgnoreProperties.class);
  }

  // 检查方法是否为Object类中的方法
  public static boolean isObjectMethod(Method method) {
    try {
      // 尝试在Object类中获取相同签名的方法
      Object.class.getMethod(method.getName(), method.getParameterTypes());
      return true;
    } catch (NoSuchMethodException e) {
      return false;
    }
  }

  /**
   * 判断方法是否为static方法
   *
   * @param method 要检查的方法
   * @return 如果是static方法返回true，否则返回false
   */
  public static boolean isStaticMethod(Method method) {
    if (method == null) return false;
    int modifiers = method.getModifiers();
    return Modifier.isStatic(modifiers);
  }

  /**
   * 判断方法是否为public
   *
   * @param method 要检查的方法
   * @return 如果是public返回true，否则返回false
   */
  public static boolean isPublic(Method method) {
    return Modifier.isPublic(method.getModifiers());
  }

  /**
   * 判断方法是否为private
   *
   * @param method 要检查的方法
   * @return 如果是private返回true，否则返回false
   */
  public static boolean isPrivate(Method method) {
    return Modifier.isPrivate(method.getModifiers());
  }

  /**
   * 判断方法是否为protected
   *
   * @param method 要检查的方法
   * @return 如果是protected返回true，否则返回false
   */
  public static boolean isProtected(Method method) {
    return Modifier.isProtected(method.getModifiers());
  }

  /**
   * 获取方法的访问修饰符的字符串表示
   *
   * @param method 要检查的方法
   * @return 访问修饰符的字符串表示
   */
  public static String getAccessModifier(Method method) {
    int modifiers = method.getModifiers();

    if (Modifier.isPublic(modifiers)) {
      return "public";
    } else if (Modifier.isPrivate(modifiers)) {
      return "private";
    } else if (Modifier.isProtected(modifiers)) {
      return "protected";
    } else {
      return "package-private"; // 默认访问级别
    }
  }

  /**
   * 判断字段是否为基础数据类型或其封装类型（包括数组、集合）
   *
   * @param field 字段对象
   * @return true 表示是基础数据类型或其封装类型（包括数组、集合）
   */
  public static boolean isPrimitiveOrWrapper(Field field) {
    Class<?> fieldType = field.getType();

    // 1. 基础数据类型
    if (fieldType.isPrimitive()) {
      return true;
    }

    // 2. 封装类型
    if (isWrapperClass(fieldType)) {
      return true;
    }

    // 3. 数组类型
    if (fieldType.isArray()) {
      Class<?> componentType = fieldType.getComponentType();
      return componentType.isPrimitive() || isWrapperClass(componentType);
    }

    return false; // 默认不是基础数据类型或其封装类型
  }

  /**
   * 判断是否为封装类型（Integer、Double 等）
   *
   * @param type 类型
   * @return true 表示是封装类型
   */
  public static boolean isWrapperClass(Class<?> type) {
    Package aPackage = type.getPackage();

    if (aPackage != null && aPackage.getName().startsWith("java.lang")) {
      return true;
    }

    return type.isPrimitive()
            || type.equals(BigDecimal.class)
            || type.equals(BigInteger.class)
            || type.equals(Date.class)
            || type.equals(LocalDate.class)
            || type.equals(LocalDateTime.class)
            || type.equals(Byte.class)
            || type.equals(MultipartFile.class);
  }

  /**
   * 判断是否为集合类型（List、Set、Map）
   *
   * @param type 类型
   * @return true 表示是集合类型
   */
  public static boolean isCollectionClassType(Class<?> type) {
    return List.class.isAssignableFrom(type)
            || Arrays.class.isAssignableFrom(type)
            || LinkedList.class.isAssignableFrom(type)
            || LinkedHashSet.class.isAssignableFrom(type)
            || Set.class.isAssignableFrom(type);
  }


  public static FieldModel getFieldModel(Field field) {
    if (null != field) {
      Class<?> fieldType = field.getType();
      String name = field.getName();
      String type = field.getType().getName();
      if (isCollectionClassType(fieldType)) {
        type = convertJvmTypeToJava(type);
      }
      return new FieldModel(name, type);
    }
    return null;
  }


  /**
   * 将 JVM 内部类型字符串转换为 Java 类型字符串
   *
   * @param jvmType JVM 内部类型字符串（如 [Ljava/lang/String; 或 I）
   * @return Java 可读的类型字符串（如 String[] 或 int）
   */
  public static String convertJvmTypeToJava(String jvmType) {
    if (jvmType == null || jvmType.isEmpty()) {
      return jvmType;
    }

    // 处理数组类型
    int arrayDimension = 0;
    while (jvmType.startsWith("[")) {
      arrayDimension++;
      jvmType = jvmType.substring(1);
    }

    // 处理基础类型
    if (PRIMITIVE_TYPE_MAP.containsKey(jvmType)) {
      String baseType = PRIMITIVE_TYPE_MAP.get(jvmType);
      return addArraySuffix(baseType, arrayDimension);
    }

    // 处理对象类型
    if (jvmType.startsWith("L") && jvmType.endsWith(";")) {
      String className = jvmType.substring(1, jvmType.length() - 1);
      className = className.replace("/", ".");
      return addArraySuffix(className, arrayDimension);
    }

    // 未知类型
    return jvmType + (arrayDimension > 0 ? "[]" : "");
  }

  /**
   * 添加数组后缀
   */
  public static String addArraySuffix(String baseType, int arrayDimension) {
    StringBuilder sb = new StringBuilder(baseType);
    for (int i = 0; i < arrayDimension; i++) {
      sb.append("[]");
    }
    return sb.toString();
  }


  public static List<FieldModel> getFieldModel(Class<?> clzz) {
    List<FieldModel> list = new ArrayList<>();

    Field[] fields = clzz.getDeclaredFields();
    if (null != fields && fields.length > 0) {
      for (Field field : fields) {
        if (!isStaticField(field)) {
          FieldModel model = analyzeField(field);
          list.add(model);
        }
      }
    }

    list = list.isEmpty() ? new ArrayList<>() : new ArrayList<>(new HashSet<>(list));
    return list;
  }

  public static Map<String, Object> getClassMap(Class<?> clzz) {

    Map<String, Object> map = new LinkedHashMap<>();

    if (checkClass(clzz)) {
      List<Class<?>> classList = getAllClass(clzz);
      List<Field> fields = getAllDeclaredFields(classList);
      List<String> ignoreProperties = getJsonIgnoreProperties(classList);
      if (CollectionUtils.isNotEmpty(fields)) {
        for (Field field : fields) {
          if (checkField(clzz, field, ignoreProperties)) {
            map.put(field.getName(), field.getType().getName());
          }
        }
      }
      map = MapUtils.isEmpty(map) ? new LinkedHashMap<>() : map;
    }
    return map;
  }

  public static boolean checkField(Class<?> clzz, Field field, List<String> ignoreProperties) {

    if (null != clzz && null != field) {
      String fieldName = field.getName();
      if (isStaticField(field))
        return false;
      if (CollectionUtils.isNotEmpty(ignoreProperties) && ignoreProperties.contains(fieldName))
        return false;
      return true;
    }
    return false;
  }

  public static boolean isStaticField(Field field) {
    int modifiers = field.getModifiers();
    boolean isStatic = Modifier.isStatic(modifiers);
    return isStatic;
  }

  public static boolean isCollectionClass(Class<?> clzz) {
    return Collection.class.isAssignableFrom(clzz) || List.class.isAssignableFrom(clzz) || Set.class.isAssignableFrom(clzz);
  }


  public static boolean isMapClass(Class<?> clazz) {

    return clazz.isAssignableFrom(Map.class)
            || clazz.isAssignableFrom(HashMap.class)
            || clazz.isAssignableFrom(LinkedHashMap.class)
            || clazz.isAssignableFrom(TreeMap.class);
  }


  public static boolean isCollectionType(Type type) {
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Type rawType = parameterizedType.getRawType();

      // 判断 rawType 是否是 Collection 或其子类（如 List、Set 等）
      return Collection.class.isAssignableFrom((Class<?>) rawType)
              || List.class.isAssignableFrom((Class<?>) rawType)
              || Arrays.class.isAssignableFrom((Class<?>) rawType)
              || ArrayList.class.isAssignableFrom((Class<?>) rawType)
              || LinkedList.class.isAssignableFrom((Class<?>) rawType)
              || LinkedHashSet.class.isAssignableFrom((Class<?>) rawType)
              || Set.class.isAssignableFrom((Class<?>) rawType);
    } else {
      ResolvableType resolvableType = ResolvableType.forType(type);
      return resolvableType.isArray()
              || resolvableType.isAssignableFrom(List.class)
              || resolvableType.isAssignableFrom(Set.class)
              || resolvableType.isAssignableFrom(ArrayList.class)
              || resolvableType.isAssignableFrom(LinkedList.class);
    }
  }

  public static boolean isMapType(Type type) {
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Type rawType = parameterizedType.getRawType();

      // 判断 rawType 是否是 Collection 或其子类（如 List、Set 等）
      return Map.class.isAssignableFrom((Class<?>) rawType);
    } else {
      ResolvableType resolvableType = ResolvableType.forType(type);
      Class<?> clazz = resolvableType.resolve();

      return isMapClass(clazz);
    }

  }


  public static FieldModel analyzeField(Field field) {
    FieldModel model = new FieldModel();
    String name = field.getName();
    model.setName(name);
    model.setField(field);
    model.setIsMap(field.getType().isAssignableFrom(Map.class));
    model.setIsCollection(field.getType().isAssignableFrom(Collection.class));

    Type genericType = field.getGenericType();
    if (genericType instanceof ParameterizedType) {
      ParameterizedType pt = (ParameterizedType) genericType;

      Type[] actualTypeArguments = pt.getActualTypeArguments();
      for (int i = 0; i < actualTypeArguments.length; i++) {
        System.out.println("泛型参数[" + i + "]: " + actualTypeArguments[i].getTypeName());
      }
      String type = Arrays.asList(actualTypeArguments).stream().map(Type::getTypeName).collect(Collectors.joining(ChartEnum.COMMA.getCode()));
      model.setIsGeneric(true);
      model.setType(type);
    } else if (genericType instanceof TypeVariable) {
      TypeVariable<?> tv = (TypeVariable<?>) genericType;
      model.setIsGeneric(true);
      model.setType(tv.getName());
    } else if (genericType instanceof GenericArrayType) {
      GenericArrayType gat = (GenericArrayType) genericType;
      Type genericComponentType = gat.getGenericComponentType();
      model.setIsGeneric(true);
      model.setType(genericComponentType.getTypeName());
    } else {
      model.setType(genericType.getTypeName());
      model.setIsGeneric(false);
    }
    return model;
  }


  public static Map<String, Object> getTypeMap(Class<?> clzz) {
    Map<String, Object> typeMap = new LinkedHashMap<>();
    List<Field> fieldList = Arrays.asList(clzz.getDeclaredFields());

    for (Field field : fieldList) {
      if (!ClassTools.isStaticField(field)) {
        field.setAccessible(true);
        Map<String, Object> objectMap = analyzeFieldType(field);
        typeMap.putAll(objectMap);
      }
    }
    return typeMap;
  }

  public static Map<String, Object> analyzeFieldType(Field field) {
    Type genericType = field.getGenericType();
    String fieldName = field.getName();
    Map<String, Object> map = new LinkedHashMap<>();
    if (genericType instanceof ParameterizedType) {
      ParameterizedType pt = (ParameterizedType) genericType;
      map.put(fieldName, pt);

    } else if (genericType instanceof TypeVariable) {
      TypeVariable<?> tv = (TypeVariable<?>) genericType;
      map.put(fieldName, tv);
    } else if (genericType instanceof GenericArrayType) {
      GenericArrayType gat = (GenericArrayType) genericType;
      Type genericComponentType = gat.getGenericComponentType();
      map.put(fieldName, genericComponentType);
    }
    return map;

  }


  public static List<String> getJsonIgnoreProperties(List<Class<?>> classList) {

    List<String> ignoreProperties = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(classList)) {
      for (Class<?> clzz : classList) {
        JsonIgnoreProperties jsonIgnoreProperties = clzz.getAnnotation(JsonIgnoreProperties.class);
        if (jsonIgnoreProperties != null) {
          String[] ignores = jsonIgnoreProperties.value();
          if (null != ignores && ignores.length > 0) {
            ignoreProperties.addAll(Arrays.asList(ignores));
          }
        }

        List<Field> fieldList = Arrays.asList(clzz.getDeclaredFields());
        for (Field field : fieldList) {
          if (isIgnore(field)) {
            ignoreProperties.add(field.getName());
          }
        }
      }
    }
    return ignoreProperties;
  }

  public static boolean isIgnore(Field field) {
    if (null == field)
      throw new NullPointerException("field is null");

    JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
    if (null != jsonIgnore) {
      if (jsonIgnore.value()) {
        return true;
      }
    }

    return false;
  }


  public static List<Class<?>> getAllClass(Class<?> clzz) {
    List<Class<?>> list = new ArrayList<>();

    while (clzz != null) {
      list.add(clzz);
      clzz = clzz.getSuperclass();
    }

    return list;
  }


  public static List<Field> getAllDeclaredFields(List<Class<?>> classList) {

    List<Field> list = new ArrayList<>();

    for (Class clzz : classList) {

      List<Field> fieldList = Arrays.asList(clzz.getDeclaredFields());

      if (CollectionUtils.isNotEmpty(fieldList)) {
        list.addAll(fieldList);
      }
    }

    return list;
  }

  public static boolean checkClass(Class<?> clzz) {
    if (clzz == null)
      return false;

    if (clzz.isEnum())
      return false;

    if (clzz.isInterface())
      return false;

    if (clzz.isAnnotation())
      return false;

    if (clzz.isPrimitive())
      return false;

    if (isWrapperClass(clzz))
      return false;

    if (isCollectionClass(clzz))
      return false;

    if (isMapClass(clzz))
      return false;

    return true;
  }


  public static Boolean isCollection(Object val) {
    if (null == val)
      return false;
    return val instanceof Collection
            || val instanceof List
            || val instanceof Set
            || val instanceof ArrayList
            || val instanceof LinkedList
            || val instanceof HashSet
            || val instanceof TreeSet
            || val instanceof Stack
            || val instanceof Vector
            || val instanceof AbstractList
            || val instanceof AbstractSequentialList
            || val instanceof AbstractCollection;

  }

  public static Boolean isMap(Object val) {
    if (null == val)
      return false;
    return val instanceof Map
            || val instanceof HashMap
            || val instanceof LinkedHashMap
            || val instanceof TreeMap
            || val instanceof Hashtable
            || val instanceof AbstractMap;
  }

  public static boolean notNull(Object data) {

    if (null == data)
      return false;
    if (isCollection(data)) {
      return CollectionUtils.isNotEmpty((Collection) data);
    }
    if (isMap(data)) {
      return MapUtils.isNotEmpty((Map) data);
    }
    if (isWrapper(data)) {
      return true;
    }
    return false;
  }

  public static boolean isWrapperType(Type type) {
    if (null == type)
      return false;
    String typeName = type.getTypeName();
    if (typeName.startsWith("java.lang"))
      return true;
    return type.equals(BigDecimal.class)
            || type.equals(BigInteger.class)
            || type.equals(Date.class)
            || type.equals(LocalDate.class)
            || type.equals(LocalDateTime.class)
            || type.equals(Byte.class)
            || type.equals(MultipartFile.class);
  }

  public static boolean isPage(Type type) {
    if (null == type)
      return false;
    String typeName = type.getTypeName();
    if (typeName.startsWith(KEY_PAGE)){
      return true;
    }
    return type.equals(Page.class)
            || type.equals(PageImpl.class) ;
  }

  public static boolean isWrapper(Object data) {
    if (null == data)
      return false;

    boolean flag = data instanceof String
            || data instanceof Integer
            || data instanceof Short
            || data instanceof Long
            || data instanceof Double
            || data instanceof Float
            || data instanceof Boolean
            || data instanceof Character
            || data instanceof Byte
            || data instanceof BigInteger
            || data instanceof BigDecimal
            || data instanceof Date
            || data instanceof LocalDate
            || data instanceof LocalDateTime
            || data instanceof MultipartFile;

    return flag;
  }

  public static boolean isVoid(Class clzz) {
    if (null == clzz)
      return false;
    String clzzName = clzz.getName().toLowerCase();
    return clzzName.equals(VOID)
            || clzzName.endsWith(VOID);
  }

  public static boolean isRequestMethod(Method method) {

    return method.isAnnotationPresent(GetMapping.class) ||
            method.isAnnotationPresent(PostMapping.class) ||
            method.isAnnotationPresent(PutMapping.class) ||
            method.isAnnotationPresent(DeleteMapping.class) ||
            method.isAnnotationPresent(PatchMapping.class) ||
            method.isAnnotationPresent(RequestMapping.class);
  }

  @SneakyThrows
  public static Class<?> forName(String className) {

    if (StringUtils.isBlank(className))
      return null;

    if (className.startsWith(KEY_PAGE)) {
      Pageable pageable = PageRequest.of(0, 10);
      Long total = 0L;
      PageImpl<?> page = new PageImpl(new ArrayList<>(), pageable, total);
      return page.getClass();
    } else {
      return Class.forName(className);
    }
  }


  @SneakyThrows
  public static Object createObject(String className) {

    if (StringUtils.isBlank(className))
      return null;

    String clzzName = className.split(TAG_START)[0];
    int indexOf = clzzName.indexOf(",");
    if (indexOf < 0) {
      if (clzzName.startsWith(KEY_PAGE)) {
        Pageable pageable = PageRequest.of(0, 10);
        Long total = 0L;
        PageImpl<?> page = new PageImpl(new ArrayList<>(), pageable, total);
        return page;
      } else if (clzzName.startsWith(KEY_LIST)) {
        return Lists.newArrayList();
      } else if (clzzName.startsWith(KEY_MAP)) {
        return new HashMap<>();
      } else {
        Class<?> clzz =  Class.forName(clzzName);
        if(isWrapperClass(clzz)){
          return clzz.newInstance();
        }else{
          return getClassMap(clzz);
        }

      }
    } else {
      String[] arrays = clzzName.split(",");
      return Arrays.asList( arrays);
    }
  }



  @SneakyThrows
  public static Class<?> getClass(String className) {
    if (StringUtils.isBlank(className))
      return null;

    String clzzName = className.split(TAG_START)[0];
    int indexOf = clzzName.indexOf(",");
    if (indexOf < 0) {
      if (clzzName.startsWith(KEY_PAGE)) {
        return PageImpl.class;
      } else if (clzzName.startsWith(KEY_LIST)) {
        return Lists.newArrayList().getClass();
      } else if (clzzName.startsWith(KEY_MAP)) {
        return new HashMap<>().getClass();
      } else {
        Class<?> clzz =  Class.forName(clzzName);
        return  clzz;
      }
    } else {
      return String[].class;
    }
  }


  public static List<Class<?>> getClassList(String className) {
    List<Class<?>> list = new ArrayList<>();
    List  <String> classNameList = getClassNameList( className);
    if(CollectionUtils.isNotEmpty(classNameList)){
      for(String clazzName:classNameList){
        Class<?> clzz = getClass(clazzName);
        list.add(clzz);
      }
    }
    return list;
  }



  public static boolean isGenericField(Class<?> clazz, String fieldName) {
    try {
      Field field = clazz.getDeclaredField(fieldName);
      Type fieldType = field.getGenericType();
      return fieldType instanceof ParameterizedType;

    } catch (NoSuchFieldException e) {
      e.printStackTrace();
      return false;
    }
  }

  public static List<String> getClassNameList(String className) {

    if (StringUtils.isBlank(className))
      return null;

    int count = StringUtils.countMatches(className, "<");

    if (count > 0) {
      List<String> list = new ArrayList<>();
      String first = StringUtils.substringBefore(className, "<");
      list.add(first);
      while (className.contains("<")) {
        className = StringUtils.substringBetweenLast(className, "<", ">");
        list.add(className);
      }
      return list;
    } else {
      return Arrays.asList(className);
    }
  }

  public static Map<String, Object> getClassNameMap(String className) {

    if (StringUtils.isBlank(className))
      return null;
    Map<String, Object> map = new LinkedHashMap<>();

    List<String> list = getClassNameList(className);
    int size = list.size();
    for (int i = 0; i < size; i++) {
      String name = list.get(i);
      if (i + 1 < size) {
        map.put(list.get(i + 1), name);
      }
    }
    return map;
  }



  private static String getClassTagName(String className) {

    return className.startsWith(TAG_START) ? StringUtils.substringBefore(className, "<") : StringUtils.substringBetween(TAG_START, TAG_END);
  }



}
