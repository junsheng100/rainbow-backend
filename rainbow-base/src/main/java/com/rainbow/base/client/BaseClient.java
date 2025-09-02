package com.rainbow.base.client;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.base.controller
 * @Filename：BaseClient
 * @Date：2025/8/17 18:17
 * @Describe:
 */
@Data
public class BaseClient {

  @Value("${server.port:80}")
  protected Integer port ;

  @Autowired
  protected HttpServletRequest request;

  @Autowired
  protected RestTemplate restTemplate;


}
