package com.rainbow.base.client;

import com.rainbow.base.config.JobConfig;
import com.rainbow.base.config.LogConfig;
import com.rainbow.base.model.vo.OperLogVo;
import com.rainbow.base.model.vo.TaskLogVo;
import com.rainbow.base.utils.IPUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.Date;
import java.util.Map;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.base.client
 * @Filename：JobTaskClient
 * @Describe:
 */
@Slf4j
@Component
public class JobTaskClient extends BaseClient {


  @Autowired
  private JobConfig config;

  @SneakyThrows
  public void sendLogMess(TaskLogVo vo) {
    if (null != vo) {
      vo.setEndTime(new Date());
      if (config.getEnabled()) {
        String logUrl = getUrl();
        restTemplate.postForObject(logUrl, vo, Map.class);
      }
    }
  }


  @SneakyThrows
  public String getUrl() {
    String logUrl = "";

    if (IPUtils.validUrl(config.getUrl())) {
      logUrl = config.getUrl();
    } else {
      InetAddress address = InetAddress.getLocalHost();
      String host = address.getHostAddress();
      String uri = config.getUrl();
      logUrl = "http://" + host + ":" + port + "/" + uri;
    }
    return logUrl;
  }


}
