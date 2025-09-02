package com.rainbow.base.model.base;

import com.rainbow.base.model.vo.PageDomain;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
public class PageData<T> extends PageDomain {

  private List<T> content;

  private Long total;

  private Integer pages;

  private Pageable pageable;


  public PageData() {

  }

  public PageData(List<T> content,  Long total, Integer pages) {
    this.content = content;
    this.total = total;
    this.pages = pages;
  }
  public PageData(List<T> content,Pageable pageable,  Long total, Integer pages) {
    this.content = content;
    this.pageable = pageable;
    this.total = total;
    this.pages = pages;
  }


  public PageData(Page<T> page) {
    this.content = page.getContent();
    this.total = page.getTotalElements();
    this.pages = page.getTotalPages();

    pageable = page.getPageable();
    super.setPageNo(pageable.getPageNumber());
    super.setPageSize(pageable.getPageSize());
    StringBuffer sbf = new StringBuffer();

    if (pageable.getSort().isSorted()) {
      pageable.getSort().stream().forEach(action -> {
        String direction = action.getDirection().toString();
        String key = action.getProperty();
        sbf.append(key).append(",").append(direction).append(",");
      });
      String sort = sbf.toString();
      sort = sort.endsWith(",") ? sort.substring(0, sort.length() - 1) : sort;
      super.setSort(sort);
    }

  }


}
