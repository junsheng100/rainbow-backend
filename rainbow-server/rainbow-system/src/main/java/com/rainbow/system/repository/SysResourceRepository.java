package com.rainbow.system.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.system.entity.SysResource;
import org.springframework.data.jpa.repository.Query;

public interface SysResourceRepository extends BaseRepository<SysResource,Long> {

  @Query("select t from SysResource t where t.fileUrl = ?1 ")
  SysResource findByFileUrl(String fileUrl);

}
