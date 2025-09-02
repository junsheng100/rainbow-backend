package com.rainbow.base.utils;

import com.rainbow.base.config.JwtConfig;
import com.rainbow.base.config.RedisTokenStore;
import com.rainbow.base.constant.DataConstant;
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
  /**
   * 统一处理 token，移除 Bearer 前缀
   * @param token 原始 token
   * @return 处理后的 token
   */
  public String cleanToken(String token) {
    if (StringUtils.isBlank(token)) {
      return null;
    }
    return token.startsWith(DataConstant.JWT_HEADER) ? token.substring(DataConstant.JWT_HEADER.length()) : token;
  }

  public String getUserIdFromToken(String token) {
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
    return Jwts.parser()
            .setSigningKey(config.getSecret())
            .parseClaimsJws(token)
            .getBody();
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public String generateAccessToken(LoginUser user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userType", user.getUserType());
    String token = doGenerateToken(claims, user.getUserId(), config.getAccessTokenValidityInSeconds());
    
    // 存储到 Redis
    redisTokenStore.storeToken(user.getUserId(), token, config.getAccessTokenValidityInSeconds());
    
    return token;
  }

  public String generateApiToken(Account account) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("id", account.getId());
    claims.put("name", account.getName());
    claims.put("loginTime", account.getLoginTime());
    String token = doGenerateToken(claims, account.getId(), config.getAccessTokenValidityInSeconds());

    // 存储到 Redis
    redisTokenStore.storeToken(account.getId(), token, config.getAccessTokenValidityInSeconds());

    return token;
  }



  public String generateRefreshToken(LoginUser user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userType", user.getUserType());
    String refreshToken = doGenerateToken(claims, user.getUserId(), config.getRefreshTokenValidityInSeconds());
    
    // 存储到 Redis
    redisTokenStore.storeRefreshToken(user.getUserId(), refreshToken, config.getRefreshTokenValidityInSeconds());
    
    return refreshToken;
  }


  public String generateRefreshApiToken(Account account) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("id", account.getId());
    claims.put("name", account.getName());
    claims.put("loginTime", account.getLoginTime());
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
    } catch (Exception e) {
      log.error("Token validation failed", e);
      return false;
    }
  }

  public boolean isToken() {
    String token = request.getHeader(config.getHeader());

    log.info("###### profile:", token);
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



}