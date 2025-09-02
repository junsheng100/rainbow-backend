package com.rainbow.base.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.rainbow.base.constant.DataConstant.*;

@Configuration
public class RestClientConfig {

  @Autowired
  private HttpServletRequest servletRequest;

  @Autowired
  private JwtConfig jwtConfig;

  @Bean
  public RestTemplate restTemplate(ClientHttpRequestInterceptor interceptor) {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getInterceptors().add(interceptor);
    return restTemplate;
  }

  @Bean
  public ClientHttpRequestInterceptor tokenInterceptor() {
    return new ClientHttpRequestInterceptor() {
      @Override
      public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String header  = jwtConfig.getHeader();
        String bearerToken = servletRequest.getHeader(header);
        request.getHeaders().add(header, bearerToken); // 设置token
        // 添加X-SERVER-REQUEST header
        request.getHeaders().add(SERVER_REQUEST_HEADER, SERVER_REQUEST_HEADER_VALUE);

        return execution.execute(request, body);
      }
    };
  }
}