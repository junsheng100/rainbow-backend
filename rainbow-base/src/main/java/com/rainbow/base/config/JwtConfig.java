package com.rainbow.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Component
@Configuration
public class JwtConfig implements Serializable {

  @Value("${jwt.url:}")
  private String url;

  @Value("${jwt.header:Authorization}")
  private String header;

  @Value("${jwt.secret:ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGH}")
  private String secret;

  @Value("${jwt.access-token-validity-in-seconds:86400}")
  private Long  accessTokenValidityInSeconds;

  @Value("${jwt.refresh-token-validity-in-seconds:604800}")
  private Long refreshTokenValidityInSeconds;


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public Long getAccessTokenValidityInSeconds() {
    return accessTokenValidityInSeconds;
  }

  public void setAccessTokenValidityInSeconds(Long accessTokenValidityInSeconds) {
    this.accessTokenValidityInSeconds = accessTokenValidityInSeconds;
  }

  public Long getRefreshTokenValidityInSeconds() {
    return refreshTokenValidityInSeconds;
  }

  public void setRefreshTokenValidityInSeconds(Long refreshTokenValidityInSeconds) {
    this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds;
  }
}
