package com.rainbow.system.resource.impl;

import com.rainbow.base.exception.DataException;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.system.entity.SysConfig;
import com.rainbow.system.repository.SysConfigRepository;
import com.rainbow.system.resource.SysConfigDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Slf4j
@Component
public class SysConfigDaoImpl extends BaseDaoImpl<SysConfig, Long, SysConfigRepository> implements SysConfigDao {


  @Override
  public SysConfig findByKey(String key) {
    Assert.notNull(key, "参数KEY不能为空");
    SysConfig data = jpaRepository.findByConfigKey(key);
    return data;
  }

  @Override
  public String getFileBasePath() {
    String fileBasePath = jpaRepository.findValueByKey("UPLOAD_PATH");
    if (StringUtils.isBlank(fileBasePath))
      throw new DataException("Data is null");

    if (fileBasePath.startsWith("${user.home}"))
      fileBasePath = fileBasePath.replace("${user.home}", System.getProperty("user.home"));
    if (fileBasePath.startsWith("${user.dir}"))
      fileBasePath = fileBasePath.replace("${user.dir}", System.getProperty("user.dir"));

    return fileBasePath;
  }
}
