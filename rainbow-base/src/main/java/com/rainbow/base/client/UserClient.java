package com.rainbow.base.client;


import com.alibaba.fastjson2.JSON;
import com.rainbow.base.config.JwtConfig;
import com.rainbow.base.constant.HttpCode;
import com.rainbow.base.model.domain.LoginUser;
import com.rainbow.base.utils.IPUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.Map;

@Slf4j
@Component
public class UserClient extends BaseClient{

  @Autowired
  private JwtConfig config;

  public LoginUser getLoginUser() {
    try {
      String userUrl = getUrl();
      Map result = restTemplate.getForObject(userUrl, Map.class);
      if (null != result) {
        Integer code = MapUtils.getInteger(result, "code");
        if (code == HttpCode.OK.value()) {
          Map data = MapUtils.getMap(result, "data");
          String jstr = JSON.toJSONString(data);
          LoginUser user = JSON.parseObject(jstr, LoginUser.class);
          return user;
        }
      }
    }catch (Exception e){
      e.printStackTrace();
      log.error(e.getMessage());
    }
    return null;
  }


  public String getUserName() {
    try {
      LoginUser userDto = getLoginUser();
      return userDto.getUserName();
    }catch (Exception e){
      return "";

    }
  }

  @SneakyThrows
  public String getUrl(){
    String userUrl = "";

    if(IPUtils.validUrl(config.getUrl())){
      userUrl = config.getUrl();
    }else {
      InetAddress address = InetAddress.getLocalHost();
      String host = address.getHostAddress();
      String uri = config.getUrl();
      userUrl = "http://" + host + ":" + port + "/" + uri;
    }
    return userUrl;
  }

}
