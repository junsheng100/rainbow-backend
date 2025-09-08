package com.rainbow.user.service.impl;

import com.rainbow.base.config.JwtConfig;
import com.rainbow.base.constant.DataConstant;
import com.rainbow.base.utils.BeanTools;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.system.entity.SysDictData;
import com.rainbow.system.resource.SysDictDataDao;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.resource.UserInfoDao;
import com.rainbow.user.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.user.service.impl
 * @Filename：AuthServiceImpl
 * @Date：2025/9/8 10:58
 * @Describe:
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

  @Autowired
  private SysDictDataDao dictDataDao;

  @Autowired
  private JwtConfig config;

  @Override
  public JwtConfig getJwtConfig() {


    String type = DataConstant.JWT_TITLE.toLowerCase();

    List<SysDictData> dataList = dictDataDao.findByDictType(type);

    if (CollectionUtils.isNotEmpty(dataList)) {
      Map<String, String> map = new HashMap<>();

      dataList.stream().forEach(data -> {
        map.put(data.getDictLabel(), data.getDictValue());
      });

      config = null == config ? new JwtConfig() : config;

      String url = MapUtils.getString(map, "url");
      String header = MapUtils.getString(map, "header");
      String secret = MapUtils.getString(map, "secret");
      String accessTokenValidityInSeconds = MapUtils.getString(map, "accessTokenValidityInSeconds");
      String refreshTokenValidityInSeconds = MapUtils.getString(map, "refreshTokenValidityInSeconds");

      config.setUrl(url);
      config.setHeader(header);
      config.setSecret(secret);
      Long accessTokenExpire = StringUtils.isBlank(accessTokenValidityInSeconds) ? null : Long.parseLong(accessTokenValidityInSeconds);
      if (null != accessTokenExpire)
        config.setAccessTokenValidityInSeconds(accessTokenExpire);
      Long refreshTokenExpire = StringUtils.isBlank(refreshTokenValidityInSeconds) ? null : Long.parseLong(refreshTokenValidityInSeconds);
      if (null != refreshTokenExpire)
        config.setAccessTokenValidityInSeconds(refreshTokenExpire);

    }

    return config;
  }
}
