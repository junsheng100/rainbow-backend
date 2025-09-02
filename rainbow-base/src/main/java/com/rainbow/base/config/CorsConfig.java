package com.rainbow.base.config;

import com.rainbow.base.enums.ChartEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "spring.mvc.cors")
public class CorsConfig {

  private String allowedOrigins;
  private String allowedMethods;
  private String allowedHeaders;
  private boolean allowCredentials;
  private Long maxAge;

  public List<String> getAllowedOriginList() {
    return new ArrayList<>(Arrays.asList(allowedOrigins.split(ChartEnum.COMMA.getCode())));
  }


  public List<String> getAllowedMethodList() {
    return new ArrayList<>(Arrays.asList(allowedMethods.split(ChartEnum.COMMA.getCode())));
  }


  public List<String> getAllowedHeaderList() {
    return new ArrayList<>(Arrays.asList(allowedHeaders.split(ChartEnum.COMMA.getCode())));
  }

}
