package com.rainbow.template.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.template.entity.TemplateField;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemplateFieldRepository extends BaseRepository<TemplateField,String> {

  @Query("select t from TemplateField t where t.entityId = ?1 ")
  List<TemplateField> findByEntityId(String entityId);

  @Query("select t from TemplateField t where t.entityId in (?1) ")
  List<TemplateField> findInEntityId(List<String> entityIdList);

  @Modifying
  @Query("delete from TemplateField where entityId = ?1 ")
  void removeByEntityId(String entityId);

  @Modifying
  @Query("delete from TemplateField where entityId in (?1) ")
  void removeInEntityId(List<String> entityIdList);

}
