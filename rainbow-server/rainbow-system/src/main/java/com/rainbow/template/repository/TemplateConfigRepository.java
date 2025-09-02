package com.rainbow.template.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.template.entity.TemplateConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemplateConfigRepository extends BaseRepository<TemplateConfig, String> {

  @Query("select t from TemplateConfig t where t.status = '0' ")
  List<TemplateConfig> findDataList();

  @Query(value = " select t from TemplateConfig t \n" +
          " where t.name like CONCAT('%',?1,'%') \n" +
          " or t.description like CONCAT('%',?1,'%') \n" +
          " or t.content  like CONCAT('%',?1,'%') \n" +
          " or t.suffix  like CONCAT('%',?1,'%') \n"  )
  Page<TemplateConfig> findPageList(String data, Pageable pageable);
}
