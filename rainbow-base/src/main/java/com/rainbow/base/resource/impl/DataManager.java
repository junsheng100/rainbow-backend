package com.rainbow.base.resource.impl;

import com.rainbow.base.annotation.Keyword;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.Origin;
import com.rainbow.base.annotation.Search;
import com.rainbow.base.annotation.SearchFilter;
import com.rainbow.base.client.UserClient;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.SearchEnum;
import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.exception.DataException;
import com.rainbow.base.model.domain.LoginUser;
import com.rainbow.base.model.base.OrderModel;
import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.utils.AnnotationProxyInjector;
import com.rainbow.base.utils.BeanTools;
import com.rainbow.base.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.criteria.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DataManager<Entity extends BaseEntity> {

  private static final Logger log = LoggerFactory.getLogger(DataManager.class);

  @Autowired
  private UserClient userClient;

  public Map<String, Predicate> commonParams(Root<Entity> root, CriteriaBuilder cb, BaseVo<Entity> vo) {
    Map<String, Predicate> params = getPredicates(root, cb, vo);
    return params;
  }

  public Map<String, Predicate> getPredicates(Root<Entity> root, CriteriaBuilder cb, BaseVo<Entity> vo) {
    Map<String, Predicate> mp = new LinkedHashMap<>();
    if (null == vo || null == vo.getData()) {
      throw new DataException("参数不能为空");
    }
    Entity entity = vo.getData();


    if (Objects.nonNull(entity)) {

      setSearchFilter(entity);

      // 递归获取所有字段属性
      List<Field> fieldList = BeanTools.getListFields(entity.getClass());

      for (Field field : fieldList) {
        String name = field.getName();
        // 排除类型
        if (excludeType(field)) {
          continue;
        }
        //对私有字段的访问取消权限检查。暴力访问。
        field.setAccessible(true);
        Object val = getFieldValue(field, entity);
        if (null != val) {
          // 排除 忽略字段
          Predicate predicate = getOnePredicate(root, cb, field, val);
          if (null != predicate) {
            mp.put(name, predicate);
          }
        }
      }
    }
    return mp;
  }


  protected void setSearchFilter(Entity entity) {
    try {
      SearchFilter searchFilter = entity.getClass().getAnnotation(SearchFilter.class);
      if (null != searchFilter) {
        Keyword[] keywords = searchFilter.value();
        for (Keyword keyword : keywords) {
          String name = keyword.key();
          if (StringUtils.isBlank(name)) {
            continue;
          }
          Field field = BeanTools.getEntityField(entity, name);
          if (null == field) {
            continue;
          }
          field.setAccessible(true);
          Object data = field.get(entity);
          if (null != data)
            continue;
          String value = keyword.value();
          if (StringUtils.isBlank(value)) {
            continue;
          }

          data = BeanTools.getFieldValue(field, value);

          if (null != data) {
            Map<String, Object> params = new HashMap<>();
            SearchEnum SELECT = keyword.SELECT();
            params.put("SELECT", SELECT);
            params.put("COLUMN", name);
            try {
              AnnotationProxyInjector.removeAnnotation(field, Search.class);
              AnnotationProxyInjector.injectAnnotation(field, Search.class, params);
              field.set(entity, data);
            } catch (Exception e) {
              log.warn("Failed to inject Search annotation: " + e.getMessage());
            }
          }
        }
      }
    } catch (Exception e) {
      log.error("Error in setSearchFilter: " + e.getMessage(), e);
    }
  }


  public Predicate getOnePredicate(Root<Entity> root, CriteriaBuilder cb, Field field, Object val) {

    Class clazz = field.getType();
    String name = field.getName();
    Search stype = field.getAnnotation(Search.class);
    Column jpaColumn = field.getAnnotation(Column.class);
    String COLUMN = null == stype ? "" : stype.COLUMN();


    name = StringUtils.isBlank(COLUMN) ? name : COLUMN;
    if (val instanceof String) {
      if (StringUtils.isBlank(String.valueOf(val))) {
        return null;
      }
    }
    Predicate predicate = null;
    if (null != stype) {
      SearchEnum searchEnum = stype.SELECT();
      predicate = getPredicate(root, cb, clazz, field, name, val, searchEnum);
    } else if (null != jpaColumn) {
      predicate = cb.equal(root.get(name).as(clazz), val);
    }
    return predicate;
  }

  protected Predicate getPredicate(Root<Entity> root, CriteriaBuilder cb, Class clazz, Field field, String name, Object val, SearchEnum searchEnum) {
    Predicate predicate = null;

    switch (searchEnum) {
      case IGNORE:
        break;
      case EQ:
        predicate = cb.equal(root.get(name).as(clazz), val);
        break;
      case IN:
        if (val instanceof ArrayList) {
          predicate = getInPredicate(root, cb, name, (List<?>) val);
        }
        if (val instanceof String) {
          String sts = String.valueOf(val);
          sts = sts.replace("[", ChartEnum.BLANK.getCode());
          sts = sts.replace("]", ChartEnum.BLANK.getCode());
          sts = sts.endsWith(ChartEnum.COMMA.getCode()) ? sts.substring(0, sts.length() - 1) : sts;
          List<String> valList = Arrays.asList(sts.split(ChartEnum.COMMA.getCode()));
          predicate = getInPredicate(root, cb, name, valList);
        }

        break;
      case LIKE:
        predicate = cb.like(root.get(name).as(clazz), "%" + val + "%");
        break;
      case LIKE_OR:
        String strs = String.valueOf(val);
        List<String> paramList = Arrays.asList(strs.split(ChartEnum.COMMA.getCode()));
        predicate = orLikeIn(root, cb, name, paramList);
        break;
      case LIKE_FRONT:
        predicate = cb.like(root.get(name).as(clazz), String.valueOf(val) + "%");
        break;
      case LIKE_AFTER:
        predicate = cb.like(root.get(name).as(clazz), "%" + String.valueOf(val));
        break;
      case GREATER:
        predicate = cb.greaterThan(root.get(name).as(clazz), (Comparable) val);
        break;
      case GREATER_EQ:
        predicate = cb.greaterThanOrEqualTo(root.get(name).as(clazz), (Comparable) val);
        break;
      case LESS:
        predicate = cb.lessThan(root.get(name).as(clazz), (Comparable) val);
        break;
      case LESS_EQ:
        predicate = cb.lessThanOrEqualTo(root.get(name).as(clazz), (Comparable) val);
        break;
      case EXISTS_LIKE:
        predicate = getExistsLikePredicate(root, cb, field, val);
        break;
      case EXISTS_EQ:
        predicate = getExistsEQPredicate(root, cb, field, val);
        break;
      default:
        predicate = cb.equal(root.get(name).as(clazz), val);
    }
    return predicate;

  }

  public Predicate orLikeIn(Root<Entity> root, CriteriaBuilder cb, String key, List<?> paramList) {
    if (null == root) {
      return null;
    }
    if (null == cb) {
      return null;
    }
    if (StringUtils.isBlank(key)) {
      return null;
    }

    List<Predicate> predList = new ArrayList<>();

    for (Object sts : paramList) {
      Predicate predicate = cb.like(root.get(key), "%" + String.valueOf(sts) + "%");
      predList.add(predicate);
    }
    Predicate[] parrs = new Predicate[predList.size()];
    predList.toArray(parrs);
    return cb.and(cb.or(parrs));
  }

  public Predicate getExistsEQPredicate(Root<Entity> root, CriteriaBuilder cb, Field field, Object val) {
    if (null == root) {
      return null;
    }
    if (null == cb) {
      return null;
    }
    if (null == val) {
      return null;
    }
    if (val instanceof String) {
      if (StringUtils.isBlank(String.valueOf(val))) {
        return null;
      }
    }
    Class clazz = field.getType();

    Origin origin = field.getAnnotation(Origin.class);

    if (null == origin)
      throw new DataException("Origin is null  ");
    Class SRC = origin.SRC();
    String join = origin.JOIN();
    String srcColumn = origin.FIELD();
    String REFER = origin.REFER();

    CriteriaQuery query = cb.createQuery(clazz);
    Subquery subQuery = query.subquery(SRC);
    Root subRoot = subQuery.from(SRC);
    Predicate p1 = cb.equal(root.get(join), subRoot.get(srcColumn));
    Predicate p2 = cb.equal(subRoot.get(REFER), val);
    subQuery.select(subRoot).where(p1, p2);

    Predicate predicate = cb.exists(subQuery);

    return predicate;
  }

  public Predicate getExistsLikePredicate(Root<Entity> root, CriteriaBuilder cb, Field field, Object val) {
    if (null == root) {
      return null;
    }
    if (null == cb) {
      return null;
    }
    if (null == val) {
      return null;
    }
    if (val instanceof String) {
      if (StringUtils.isBlank(String.valueOf(val))) {
        return null;
      }
    }
    Class clazz = root.getJavaType();

    Origin origin = field.getAnnotation(Origin.class);

    if (null == origin)
      throw new DataException("Origin is null  ");
    Class SRC = origin.SRC();
    String join = origin.JOIN();
    String srcColumn = origin.FIELD();
    String REFER = origin.REFER();

    CriteriaQuery query = cb.createQuery(clazz);
    Subquery subQuery = query.subquery(SRC);
    Root subRoot = subQuery.from(SRC);
    Predicate p1 = cb.equal(root.get(join), subRoot.get(srcColumn));
    Predicate p2 = cb.like(subRoot.get(REFER), "%" + val + "%");
    subQuery.select(subRoot).where(p1, p2);

    Predicate predicate = cb.exists(subQuery);

    return predicate;
  }

  public Predicate getInPredicate(Root<Entity> root, CriteriaBuilder cb, String key, List<?> paramList) {
    if (null == root) {
      return null;
    }
    if (null == cb) {
      return null;
    }
    if (StringUtils.isBlank(key)) {
      return null;
    }
    if (CollectionUtils.isNotEmpty(paramList)) {

      if (paramList.size() > 1) {
        Path<Object> path = root.get(key);
        CriteriaBuilder.In<Object> in = cb.in(path);
        for (Object sts : paramList) {
          in.value(sts);
        }
        return cb.and(in);
      } else {
        Predicate predicate = cb.equal(root.get(key), paramList.get(0));
        return predicate;
      }
    }

    return cb.equal(root.get(key), Integer.MIN_VALUE);
  }

  public boolean excludeType(Field field) {

    Annotation search = field.getAnnotation(Search.class);
    if (search instanceof Search) {
      SearchEnum searchEnum = ((Search) search).SELECT();
      if (SearchEnum.IGNORE.equals(searchEnum))
        return Boolean.TRUE;
    } else {
      Annotation tran = field.getAnnotation(Transient.class);
      if (tran instanceof Transient) {
        return Boolean.TRUE;
      }
    }
    // 默认
    return Boolean.FALSE;
  }


  public Entity addEntity(Entity entity) {
    if (null == entity) {
      throw new DataException("参数不能为空");
    }

    addNormal(entity);

    String userName = userClient.getUserName();
    entity.setFcu(userName);
    entity.setLcu(userName);

    return entity;
  }


  public Entity modEntity(Entity entity, Entity old) {

    try {
      modNormal(entity, old);

      LoginUser user = userClient.getLoginUser();
      String userName = user.getUserName();
      entity.setLcu(userName);
      if (StringUtils.isBlank(entity.getFcu())) {
        entity.setFcu(userName);
      }

    } catch (Exception e) {
      log.error(e.getMessage());
      throw new DataException(e.getMessage());
    }
    return entity;
  }

  public Entity modNormal(Entity entity, Entity old) {
    if (null == entity)
      throw new DataException("参数不能为空");

    if (null == old)
      throw new DataException("数据不能为空");

    try {
      BeanUtils.copyProperties(entity, old, CommonUtils.getNullPropertyNames(entity));
      BeanUtils.copyProperties(old, entity, CommonUtils.getNullPropertyNames(old));

      LocalDateTime now = LocalDateTime.now();
      entity.setLcd(now);

      if (null != entity.getFcd()) {
        entity.setFcd(now);
      }

    } catch (Exception e) {
      log.error(e.getMessage());
      throw new DataException(e.getMessage());
    }
    return entity;
  }


  public Entity addNormal(Entity entity) {
    if (null == entity) {
      throw new DataException("参数不能为空");
    }

    BeanTools.setIdValue(entity);
    String status = entity.getStatus();
    status = StringUtils.isBlank(status) ? UseStatus.NO.getCode() : status;
    entity.setStatus(status);
    LocalDateTime now = LocalDateTime.now();
    entity.setFcd(now);
    entity.setLcd(now);

    return entity;
  }

  public List<Predicate> getCommonPredicates(Map<String, Predicate> mp,
                                             Root<Entity> root,
                                             CriteriaBuilder cb,
                                             BaseVo<Entity> vo) {
    return commonPredicates(mp);
  }

  public List<Predicate> commonPredicates(Map<String, Predicate> mp) {
    List<Predicate> predicateList = null;

    if (Objects.nonNull(mp)) {
      predicateList = new ArrayList<>();
      Iterator<String> iterator = mp.keySet().iterator();

      while (iterator.hasNext()) {
        String key = iterator.next();
        Predicate predicate = mp.get(key);
        predicateList.add(predicate);
      }

    }
    return predicateList;
  }

  public Object getFieldValue(Field field, Entity entity) {
    Object val = null;
    try {
      val = field.get(entity);
      if (val instanceof String)
        val = ((String) val).trim();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return val;
  }

  public PageRequest getPageRequest(BaseVo<Entity> vo) {
    Sort sort = getBizSort(vo);
    Integer pageNo = vo.getPageNo();
    pageNo = pageNo <= 0 ? 0 : pageNo - 1;
    Integer pageSize = vo.getPageSize();
    PageRequest pageable = PageRequest.of(pageNo, pageSize, sort);
    return pageable;
  }

  public Sort getBizSort(BaseVo<Entity> vo) {

    String sortStr = vo.getSort();

    Sort mySort = null;

    List<OrderModel> orderList = StringUtils.isNotBlank(sortStr) ? getRequestSort(sortStr) : new ArrayList<>();

    Entity entity = vo.getData();
    if (Objects.nonNull(entity)) {
      // 递归获取所有字段属性
      List<OrderModel> entityOrderModelList = getEntityOrderModel(entity.getClass());

      if (CollectionUtils.isNotEmpty(entityOrderModelList)) {
        List<Sort.Order> hasOrderList = orderList.stream().map(t -> {
          return t.getOrder();
        }).collect(Collectors.toList());
        entityOrderModelList = entityOrderModelList.stream().filter(t -> !hasOrderList.contains(t)).collect(Collectors.toList());
      }
      if (CollectionUtils.isNotEmpty(entityOrderModelList))
        orderList.addAll(entityOrderModelList);
    }

    if (CollectionUtils.isNotEmpty(orderList)) {
      orderList.sort(new Comparator<OrderModel>() {
        @Override
        public int compare(OrderModel o1, OrderModel o2) {
          return o1.getIndex() - o2.getIndex();
        }
      });
      List<Sort.Order> orderByList = orderList.stream().map(OrderModel::getOrder).collect(Collectors.toList());
      mySort = Sort.by(orderByList);
      return mySort;
    }

    return mySort;
  }


  public Sort getCommonSort(CommonVo<?> vo,Class clss) {

    String sortStr = vo.getSort();

    Sort mySort = null;

    List<OrderModel> orderList = StringUtils.isNotBlank(sortStr) ? getRequestSort(sortStr) : new ArrayList<>();

    if (null != clss) {
      // 递归获取所有字段属性
      List<OrderModel> entityOrderModelList = getEntityOrderModel(clss);
      if (CollectionUtils.isNotEmpty(entityOrderModelList)) {
        List<Sort.Order> hasOrderList = orderList.stream().map(t -> {
          return t.getOrder();
        }).collect(Collectors.toList());
        entityOrderModelList = entityOrderModelList.stream().filter(t -> !hasOrderList.contains(t)).collect(Collectors.toList());
      }
      if (CollectionUtils.isNotEmpty(entityOrderModelList))
        orderList.addAll(entityOrderModelList);
    }

    if (CollectionUtils.isNotEmpty(orderList)) {
      orderList.sort(new Comparator<OrderModel>() {
        @Override
        public int compare(OrderModel o1, OrderModel o2) {
          return o1.getIndex() - o2.getIndex();
        }
      });
      List<Sort.Order> orderByList = orderList.stream().map(OrderModel::getOrder).collect(Collectors.toList());
      mySort = Sort.by(orderByList);
      return mySort;
    }

    return mySort;
  }

  public List<OrderModel> getRequestSort(String sortStr) {
    List<OrderModel> orderList = new ArrayList<>();
    if (StringUtils.isNotBlank(sortStr)) {
      orderList = new ArrayList<>();
      String[] sts = sortStr.split(ChartEnum.COMMA.getCode());
      int len = sts.length;
      int index = 0;

      while (index < len) {
        String name = sts[index];
        if (null != isSortDirection(name)) {
          index++;
          continue;
        }
        String sc = null;
        Sort.Direction val = null;
        if (index + 1 < len) {
          sc = sts[index + 1];
        }
        val = isSortDirection(sc);
        if (null == val) {
          val = Sort.Direction.ASC;
        }
        Sort.Order order = new Sort.Order(val, name);
        OrderModel model = new OrderModel(index, order);
        orderList.add(model);
        index++;
      }

    }
    return orderList;

  }


  public Sort.Direction isSortDirection(String val) {
    if (StringUtils.isNotBlank(val)) {
      if (val.toUpperCase().equals(Sort.Direction.ASC.name().toUpperCase())) {
        return Sort.Direction.ASC;
      }
      if (val.toUpperCase().equals(Sort.Direction.DESC.name().toUpperCase())) {
        return Sort.Direction.DESC;
      }
    }
    return null;
  }

  public Sort getEntitySort(Class clss) {

    List<Field> fieldList = BeanTools.findFields(clss, OrderBy.class);
    if (CollectionUtils.isNotEmpty(fieldList)) {
      List<OrderModel> orderList = getEntityOrderModel(clss);
      orderList.sort(new Comparator<OrderModel>() {
        @Override
        public int compare(OrderModel o1, OrderModel o2) {
          return o1.getIndex() - o2.getIndex();
        }
      });
      List<Sort.Order> orderByList = orderList.stream().map(OrderModel::getOrder).collect(Collectors.toList());

      Sort sort = Sort.by(orderByList);
      return sort;
    }
    return null;
  }


  public List<OrderModel> getEntityOrderModel(Class clss) {

    List<Field> fieldList = BeanTools.findFields(clss, OrderBy.class);
    if (CollectionUtils.isNotEmpty(fieldList)) {
      List<OrderModel> orderList = new ArrayList<>();
      for (Field field : fieldList) {
        OrderBy orderBy = field.getAnnotation(OrderBy.class);
        String fieldName = field.getName();
        Sort.Order order = new Sort.Order(orderBy.value(), fieldName);
        Integer index = Integer.valueOf(orderBy.INDEX());
        OrderModel model = new OrderModel(index, order);
        orderList.add(model);
      }
      return orderList;
    }
    return null;
  }


  public String getUserName() {
    String userName = userClient.getUserName();
    return userName;
  }

  public LoginUser getLoginUser() {
    LoginUser user = userClient.getLoginUser();
    return user;
  }


  public <ID, Entity> ID getIdValue(Entity entity, Class<Entity> clss) {
    List<Field> fieldList = BeanTools.findFields(clss, Id.class);
    if (CollectionUtils.isNotEmpty(fieldList)) {
      Field field = fieldList.get(0);
      field.setAccessible(true);
      try {
        return (ID) field.get(entity);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public String extractFromClause(String sql) {
    int fromIndex = sql.toLowerCase().indexOf("from");
    if (fromIndex != -1) {
      return sql.substring(fromIndex);
    }
    return sql;
  }

  public boolean isDefaultMethodOverridden(Class<?> implClass,
                                           Method interfaceMethod) {
    if (!interfaceMethod.isDefault()) {
      return false;
    }

    try {
      Method implMethod = implClass.getMethod(
              interfaceMethod.getName(),
              interfaceMethod.getParameterTypes());

      // 如果实现方法与接口默认方法不同，则认为是重写
      return !implMethod.equals(interfaceMethod);
    } catch (NoSuchMethodException e) {
      return false; // 没有重写，使用默认实现
    }
  }

  public Pageable getCommonPageable(CommonVo<?> vo, Class<Entity> clzz) {

    Sort sort = getCommonSort(vo,clzz);
    Integer pageNo = vo.getPageNo();
    pageNo = pageNo <= 0 ? 0 : pageNo - 1;
    Integer pageSize = vo.getPageSize();
    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
    return pageable;
  }
}
