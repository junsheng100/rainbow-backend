package com.rainbow.files.resource.impl;

import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.exception.DataException;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.RandomId;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.files.entity.SysResource;
import com.rainbow.files.repository.SysResourceRepository;
import com.rainbow.files.resource.SysResourceDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Component
public class SysResourceDaoImpl extends BaseDaoImpl<SysResource,String, SysResourceRepository> implements SysResourceDao {
  @Override
  public SysResource findByFileUrl(String fileUrl) {
    return StringUtils.isBlank(fileUrl)?null:jpaRepository.findByFileUrl(fileUrl);
  }

  @Override
  public SysResource findByMd5Code(String md5Code) {
    return StringUtils.isBlank(md5Code)?null:jpaRepository.findByMd5Code(md5Code);
  }


}
