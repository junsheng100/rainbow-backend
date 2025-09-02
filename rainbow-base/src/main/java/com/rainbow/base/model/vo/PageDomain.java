package com.rainbow.base.model.vo;


import com.rainbow.base.model.base.OrderModel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PageDomain implements Serializable {

  private Integer pageNo;
  private Integer pageSize;
  private String sort;


  public PageDomain() {
  }

  public PageDomain(Integer pageNo, Integer pageSize) {
    this.pageNo = pageNo;
    this.pageSize = pageSize;
    this.sort = sort;
  }
  public PageDomain(Integer pageNo, Integer pageSize, String sort) {
    this.pageNo = pageNo;
    this.pageSize = pageSize;
    this.sort = sort;
  }

  public static PageRequest Of(Integer pageNo, Integer pageSize, String orderBy) {
    Sort sort = getRequestSort(orderBy);
    return PageRequest.of(pageNo, pageSize, sort);
  }

  public static Sort getRequestSort(String sortStr) {

    Sort mySort = null;
    List<OrderModel> orderList = null;
    if (StringUtils.isNotBlank(sortStr)) {
      orderList = new ArrayList<>();
      String[] sts = sortStr.split(",");
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
    }
    return mySort;

  }

  public static Sort.Direction isSortDirection(String val) {
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

}