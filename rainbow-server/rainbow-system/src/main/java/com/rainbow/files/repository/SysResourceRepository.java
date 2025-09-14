package com.rainbow.files.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.files.entity.SysResource;
import org.springframework.data.jpa.repository.Query;

public interface SysResourceRepository extends BaseRepository<SysResource,String> {

  @Query("select t from SysResource t where t.fileUrl = ?1 ")
  SysResource findByFileUrl(String fileUrl);

  SysResource findByMd5Code(String md5Code);
}
