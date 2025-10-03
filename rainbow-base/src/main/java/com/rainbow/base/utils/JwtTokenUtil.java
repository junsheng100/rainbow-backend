package com.rainbow.base.utils;

import com.alibaba.fastjson2.JSON;
import com.rainbow.base.config.JwtConfig;
import com.rainbow.base.config.RedisTokenStore;
import com.rainbow.base.constant.DataConstant;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.exception.NoLoginException;
import com.rainbow.base.model.domain.Account;
import com.rainbow.base.model.domain.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtTokenUtil {

  @Autowired
  private JwtConfig config;

  @Autowired
  private RedisTokenStore redisTokenStore;

  @Autowired
  protected HttpServletRequest request;


  private final static String USER_ID = "userId";
  private final static String USER_NAME = "userName";
  private final static String USER_TYPE = "userType";
  private final static String ACCOUNT_ID = "id";
  private final static String ACCOUNT_NAME = "name";
  private final static String ACCOUNT_LOGIN_TIME = "loginTime";


  /**
   * 统一处理 token，移除 Bearer 前缀
   *
   * @param token 原始 token
   * @return 处理后的 token
   */
  public String cleanToken(String token) {
    if (StringUtils.isBlank(token)) {
      return null;
    }
    if (token.startsWith(DataConstant.JWT_HEADER)) {
      token = token.substring(DataConstant.JWT_HEADER.length());
    }
    token = token.trim();
    return token;
  }

  public String getUserIdFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public String getUserIdFromToken() {
    String token = getToken();
    return getClaimFromToken(token, Claims::getSubject);
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  public Claims getAllClaimsFromToken(String token) {
    token = cleanToken(token);
    return Jwts.parser()
            .setSigningKey(config.getSecret())
            .parseClaimsJws(token)
            .getBody();
  }


  public Claims getAllClaimsFromToken() {
    String token = request.getHeader(config.getHeader());
    token = cleanToken(token);
    return getAllClaimsFromToken(token);
  }

  public String getUserId() {
    LoginUser user = getLoginUser();
    String userId = user.getUserId();
    return userId;
  }

  public String getUserName() {
    LoginUser user = getLoginUser();
    String userName = user.getUserName();
    return userName;
  }


  public LoginUser getLoginUser() {
    String token = getToken();
    Claims claims = getAllClaimsFromToken(token);
    LoginUser user = JSON.parseObject(JSON.toJSONString(claims), LoginUser.class);
    return user;
  }


  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public String generateAccessToken(LoginUser user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(USER_TYPE, user.getUserType());
    claims.put(USER_NAME, user.getUserName());
    claims.put(USER_ID, user.getUserId());
    String token = doGenerateToken(claims, user.getUserId(), config.getAccessTokenValidityInSeconds());

    // 存储到 Redis
    redisTokenStore.storeToken(user.getUserId(), token, config.getAccessTokenValidityInSeconds());

    return token;
  }

  public String generateApiToken(Account account) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(ACCOUNT_ID, account.getId());
    claims.put(ACCOUNT_NAME, account.getName());
    claims.put(ACCOUNT_LOGIN_TIME, account.getLoginTime());
    String token = doGenerateToken(claims, account.getId(), config.getAccessTokenValidityInSeconds());

    // 存储到 Redis
    redisTokenStore.storeToken(account.getId(), token, config.getAccessTokenValidityInSeconds());

    return token;
  }


  public String generateRefreshToken(LoginUser user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(USER_TYPE, user.getUserType());
    claims.put(USER_NAME, user.getUserName());
    claims.put(USER_ID, user.getUserId());
    String refreshToken = doGenerateToken(claims, user.getUserId(), config.getRefreshTokenValidityInSeconds());

    // 存储到 Redis
    redisTokenStore.storeRefreshToken(user.getUserId(), refreshToken, config.getRefreshTokenValidityInSeconds());

    return refreshToken;
  }


  public String generateRefreshApiToken(Account account) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(ACCOUNT_ID, account.getId());
    claims.put(ACCOUNT_NAME, account.getName());
    claims.put(ACCOUNT_LOGIN_TIME, account.getLoginTime());
    String refreshToken = doGenerateToken(claims, account.getId(), config.getRefreshTokenValidityInSeconds());

    // 存储到 Redis
    redisTokenStore.storeRefreshToken(account.getId(), refreshToken, config.getRefreshTokenValidityInSeconds());

    return refreshToken;
  }

  public String doGenerateToken(Map<String, Object> claims, String subject, Long validity) {
    JwtBuilder tokenBuilder = Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + validity * 1000))
            .signWith(SignatureAlgorithm.HS512, config.getSecret());
    return tokenBuilder.compact();
  }

  public boolean validateToken(String token) {

    try {
      token = cleanToken(token);
      if (StringUtils.isBlank(token)) {
        return false;
      }
      Claims claims = Jwts.parser()
              .setSigningKey(config.getSecret())
              .parseClaimsJws(token)
              .getBody();
      String userId = claims.getSubject();
      // 检查 token 是否过期
      if (claims.getExpiration().before(new Date())) {
        return false;
      }
      // 验证 Redis 中存储的 token
      return redisTokenStore.validateToken(userId, token);
    } catch (io.jsonwebtoken.SignatureException e) {
      log.error("JWT signature validation failed for token: {}", token);
      return false;
    } catch (Exception e) {
      log.error("Token validation failed", e);
      return false;
    }
  }


  public boolean isToken() {
//    String token = request.getHeader(config.getHeader());
    String token = getToken();
    // 使用统一的方法处理 token
    token = cleanToken(token);
    // 验证令牌
    Assert.notNull(token, "无效的令牌");
    Assert.isTrue(!validateToken(token), "无效的令牌");

    return true;
  }

  public Long getAccessTokenValidityInSeconds() {
    return config.getAccessTokenValidityInSeconds();
  }

  public Long getRefreshTokenValidityInSeconds() {
    return config.getRefreshTokenValidityInSeconds();
  }

  public JwtConfig getConfig() {
    return config;
  }

  public void setConfig(JwtConfig config) {
    this.config = config;
  }

  private String getToken() {
    String token = request.getHeader(config.getHeader());
    if (StringUtils.isNotBlank(token)) {
      token = token.replace(" ",ChartEnum.BLANK.getCode());
      return token;
    }
    throw new NoLoginException("NOT LOGIN");
  }
}