package com.rainbow.system.service.impl;

import com.rainbow.base.constant.SettingConstants;
import com.rainbow.base.service.RedisService;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.system.entity.SysConfig;
import com.rainbow.system.resource.SysConfigDao;
import com.rainbow.system.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Service
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfig, Long, SysConfigDao> implements SysConfigService {

  @Autowired
  private RedisService redisService;

  @Override
  public SysConfig store(@Valid SysConfig entity) {
    String key = getConfigCacheKey(entity.getConfigKey());
    baseDao.store(entity);
    redisService.setCacheObject(key, entity.getConfigValue());
    return entity;
  }


  @Override
  public Boolean delete(Long id) {
    return baseDao.remove(id);
  }

  @Override
  public Boolean deleteInBatch(List<Long> data) {

    if (CollectionUtils.isNotEmpty(data)) {
      for (int i = 0; i < data.size(); i++) {
        String value = String.valueOf(data.get(i));
        Long id = Long.valueOf(value);
        baseDao.remove(id);
      }
      return true;
    }
    return false;
  }

  private String getConfigCacheKey(String configKey) {
    return SettingConstants.BASE_KEY + ":" + configKey;
  }

  @Override
  public SysConfig getConfigValue(String configKey) {
    return baseDao.findByKey(configKey);
  }
}
