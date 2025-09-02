package com.rainbow.template.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.template.entity.TemplateEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemplateEntityRepository extends BaseRepository<TemplateEntity, String> {

  @Query("select t from TemplateEntity t where t.status = '0' ")
  List<TemplateEntity> findDataList();
}
