package com.rainbow.base.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
public class JwtConfig {

  @Value("${jwt.url:}")
  private String url;

  @Value("${jwt.header:Authorization}")
  private String header;

  @Value("${jwt.secret:}")
  private String secret;

  @Value("${jwt.access-token-validity-in-seconds:1900}")
  private Long  accessTokenValidityInSeconds;

  @Value("${jwt.refresh-token-validity-in-seconds:87400}")
  private Long refreshTokenValidityInSeconds;



}
