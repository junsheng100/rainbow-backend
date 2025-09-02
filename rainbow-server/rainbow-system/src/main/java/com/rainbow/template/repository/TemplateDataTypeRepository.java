package com.rainbow.template.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.template.entity.TemplateDataType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemplateDataTypeRepository extends BaseRepository<TemplateDataType, String> {

  @Query(value = " select t from TemplateDataType t \n" +
          " where t.dbType like CONCAT('%',?1,'%') \n" +
          " or t.dataType like CONCAT('%',?1,'%') \n" +
          " or t.columnType  like CONCAT('%',?1,'%') \n" +
          " or t.dataRange  like CONCAT('%',?1,'%') \n" +
          " or t.dataDescribe  like CONCAT('%',?1,'%') \n" +
          " or t.javaType  like CONCAT('%',?1,'%') \n" )
  Page<TemplateDataType> findPageList(String data, Pageable pageable);

  @Query(value = " select distinct dbType from TemplateDataType t where t.status = '0' ")
  List<String> findDbGroup();

  @Query(value = " select t from TemplateDataType t where t.status = '0' and t.dbType = ?1 ")
  List<TemplateDataType> findByDbType(String dbType);

  @Query(value = " select max(t.orderNum) from TemplateDataType t where t.status = '0' ")
  Integer getMaxOrderNum();

  @Query(value = " select t from TemplateDataType t where t.status = '0' and  dbType= ?1 and  columnType = ?2 ")
  TemplateDataType findType(String dbType, String columnType);

}
