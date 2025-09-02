package com.rainbow.base.client;


import com.rainbow.base.config.LogConfig;
import com.rainbow.base.model.vo.OperLogVo;
import com.rainbow.base.utils.IPUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Map;

@Slf4j
@Component
@RequestScope
public class OperLogClient extends BaseClient{

  @Autowired
  private LogConfig config;

  @SneakyThrows
  public void sendLogMess(OperLogVo vo) {
    if(config.getEnabled()) {
      String logUrl = getUrl();
      restTemplate.postForObject(logUrl, vo, Map.class);
    }
  }

  public void sendLogMess(String logUrl,OperLogVo vo) {
    restTemplate.postForObject(logUrl,vo,Map.class);
  }

  @SneakyThrows
  public String getUrl(){
    String logUrl = "";

    if(IPUtils.validUrl(config.getUrl())){
      logUrl = config.getUrl();
    }else {
      InetAddress address = InetAddress.getLocalHost();
      String host = address.getHostAddress();
      String uri = config.getUrl();
      logUrl = "http://" + host + ":" + port + "/" + uri;
    }
    return logUrl;
  }

}
