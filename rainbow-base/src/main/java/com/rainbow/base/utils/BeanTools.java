package com.rainbow.base.utils;

import com.rainbow.base.annotation.Search;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.enums.SearchEnum;
import com.rainbow.base.exception.DataException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName BeanTool
 * @Description TODO
 * @Author shijunliu
 * @Date 2021/12/5 2:58 下午
 * @Version 1.0
 */
@Slf4j
public class BeanTools {


  public static List<Field> getListFields(Class clss) {
    List<Field> list = new ArrayList<>();

    while (clss != null) {
      Field[] fields = clss.getDeclaredFields();
      if (null != fields || fields.length > 0) {

        for (Field field : fields) {
          Search stype = field.getAnnotation(Search.class);
          if (null != stype) {
            SearchEnum searchEnum = stype.SELECT();
            if (!SearchEnum.IGNORE.equals(searchEnum)) {
              list.add(field);
            }
          }

          Column column = field.getAnnotation(Column.class);
          if (null != column) {
            if (!list.contains(field)) {
              list.add(field);
            }
          }
        }

      }
      clss = clss.getSuperclass();
    }
    list = new ArrayList<>(new HashSet<>(list));
    return list;
  }


  public static List<Field> findFields(Class clss, Class... include) {
    List<Field> list = new ArrayList<>();

    while (clss != null) {
      Field[] fields = clss.getDeclaredFields();
      if (null != fields || fields.length > 0) {
        for (Field field : fields) {
          for (Class clzz : include) {
            Object any = field.getAnnotation(clzz);
            if (null != any) {
              list.add(field);
            }
          }
        }
      }
      clss = clss.getSuperclass();
    }
    list = new ArrayList<>(new HashSet<>(list));
    return list;
  }

  public static Object setIdValue(Object data) {
    List<Field> idList = getAnnotationFields(data.getClass(), Id.class);
    Object id = null;
    try {
      if (CollectionUtils.isNotEmpty(idList)) {
        for (Field field : idList) {
          field.setAccessible(true);
          id = field.get(data);
          if (null == id || StringUtils.isBlank(id.toString())) {
            GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
            if (null == generatedValue) {
              id = createIdDataValue(data, field);
            } else {
              GenerationType generationType = generatedValue.strategy();
              if (null == generationType || GenerationType.AUTO == generationType) {
                id = createIdDataValue(data, field);
              }
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return id;
  }

  public static Object createIdDataValue(Object data, Field field) {
    if (null == data)
      return null;
    if (null == field)
      return null;
    Object id = null;
    try {
      Column column = field.getAnnotation(Column.class);
      if (null == column)
        return null;
      int length = column.length();

      Class clzz = field.getType();
      field.setAccessible(true);

      if (clzz == long.class || clzz == Long.class) {
        length = length > 20 ? 20 : length;
        int seqNo = length - 15;
        BigInteger idValue = RandomId.getSeqNo(seqNo);
        id = idValue.longValue();
        field.set(data, id);
      } else if (clzz == String.class) {
        length = length % 2 == 0 ? length / 2 : (length / 2) - 1;
        length = length > 32 ? 32 : length;
        id = RandomId.generateShortUuid(length);
        field.set(data, id);
      } else if (clzz == BigInteger.class) {
        int seqNo = length - 15;
        id = RandomId.getSeqNo(seqNo);
        field.set(data, id);
      } else {
        throw new DataException("ID is only supported  Typeof [String , Long, BigInteger ] ");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return id;

  }


  public static List<Field> getAnnotationFields(Class beanClass, Class srcClzz) {
    List<Field> list = new ArrayList<>();

    while (beanClass != null) {
      Field[] fields = beanClass.getDeclaredFields();
      for (Field field : fields) {
        Object stype = field.getAnnotation(srcClzz);
        if (null != stype) {
          list.add(field);
        }
      }
      beanClass = beanClass.getSuperclass();
    }
    list = new ArrayList<>(new HashSet<>(list));
    return list;
  }

  public static String getColumnName(Field field) {
    if (null == field)
      throw new NullPointerException("field is null");
    Column col = field.getAnnotation(Column.class);
    String name = col.name();
    name = StringUtils.isBlank(name) ? toSnakeCase(field.getName()) : name;
    return name;
  }

  public static String toSnakeCase(String input) {
    return input.replaceAll("[A-Z]", "_$0").toLowerCase().replaceFirst("_", "");
  }


  public static <Entity extends BaseEntity> Entity clonePk(Entity entity, Class clzz) {
    try {
      if (null != entity) {
        List<Field> fieldList = getAnnotationFields(entity.getClass(), clzz);
        if (CollectionUtils.isEmpty(fieldList))
          return null;
        Entity next = (Entity) entity.getClass().newInstance();
        for (Field field : fieldList) {
          field.setAccessible(true);
          Object val = field.get(entity);
          if (isEmpty(val))
            return null;
          field.set(next, val);
        }
        return next;
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new DataException(e.getMessage());
    }
    return null;
  }

  public static boolean isEmpty(Object data) {
    if (Objects.isNull(data))
      return true;
    if (data instanceof String) {
      if (ChartEnum.BLANK.getCode().equals(data.toString())) {
        return true;
      }
    }
    return false;
  }

  public static boolean isNotEmpty(Object data) {
    return !isEmpty(data);
  }


  public static boolean isPrimitiveWrapper(Object obj) {
    return obj instanceof Byte ||
            obj instanceof Short ||
            obj instanceof Integer ||
            obj instanceof Long ||
            obj instanceof Float ||
            obj instanceof Double ||
            obj instanceof Character ||
            obj instanceof String ||
            obj instanceof Boolean;
  }


  public static Field getEntityField(Object entity, String name) {

    if (null == entity)
      return null;
    if (StringUtils.isBlank(name))
      return null;

    Class clss = entity.getClass();
    while (clss != null) {
      Field[] fields = clss.getDeclaredFields();
      if (null != fields) {
        for (Field field : fields) {
          if (name.equals(field.getName())) {
            return field;
          }
        }

      }
      clss = clss.getSuperclass();
    }
    return null;

  }

  public static Object getFieldValue(Field field, String value) {
    if (null == field)
      return null;
    if (StringUtils.isBlank(value))
      return null;
    Class clzz = field.getType();
    field.setAccessible(true);
    try {
      if (clzz == String.class) {
        return value;
      } else if (clzz == int.class || clzz == Integer.class) {
        return Integer.parseInt(value);
      } else if (clzz == long.class || clzz == Long.class) {
        return Long.parseLong(value);
      } else if (clzz == float.class || clzz == Float.class) {
        return Float.parseFloat(value);
      } else if (clzz == double.class || clzz == Double.class) {
        return Double.parseDouble(value);
      } else if (clzz == boolean.class || clzz == Boolean.class) {
        return Boolean.parseBoolean(value);
      } else if (clzz == BigInteger.class) {
        return new BigInteger(value);
      } else if (clzz == BigDecimal.class) {
        return new BigDecimal(value);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return null;
  }



}
