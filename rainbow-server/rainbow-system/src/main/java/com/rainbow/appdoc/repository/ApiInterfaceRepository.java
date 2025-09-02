package com.rainbow.appdoc.repository;

import com.rainbow.appdoc.entity.AppInterface;
import com.rainbow.base.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApiInterfaceRepository extends BaseRepository<AppInterface, String> {


  List<AppInterface> findByCategoryId(String categoryId);

  @Query("select t from AppInterface t where t.categoryId in (?1) and t.status = '0' and t.disabled = 0 ")
  List<AppInterface> findInCategoryIds(List<String> categoryIds);

  @Query("select t from AppInterface t where t.id not in (?1) ")
  List<AppInterface> findNotInId(List<String> idList);

}