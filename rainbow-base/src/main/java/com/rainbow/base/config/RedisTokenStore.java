package com.rainbow.base.config;

import com.rainbow.base.constant.CacheConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;

import java.util.concurrent.TimeUnit;

import static com.rainbow.base.constant.DataConstant.JWT_HEADER;

@Slf4j
@Component
public class RedisTokenStore {

  private static final String TOKEN_KEY_PREFIX = CacheConstants.TOKEN_KEY_PREFIX;
  private static final String REFRESH_TOKEN_KEY_PREFIX = CacheConstants.REFRESH_TOKEN_KEY_PREFIX;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  public void storeToken(String userId, String token, long expirationInSeconds) {
    String key = TOKEN_KEY_PREFIX + userId;
    redisTemplate.opsForValue().set(key, token, expirationInSeconds, TimeUnit.SECONDS);
  }

  public void storeRefreshToken(String userId, String refreshToken, long expirationInSeconds) {
    String key = REFRESH_TOKEN_KEY_PREFIX + userId;
    redisTemplate.opsForValue().set(key, refreshToken, expirationInSeconds, TimeUnit.SECONDS);
  }

  public String getToken(String userId) {
    String key = TOKEN_KEY_PREFIX + userId;
    return redisTemplate.opsForValue().get(key);
  }

  public String getRefreshToken(String userId) {
    String key = REFRESH_TOKEN_KEY_PREFIX + userId;
    return redisTemplate.opsForValue().get(key);
  }

  public void removeToken(String userId) {
    String key = TOKEN_KEY_PREFIX + userId;
    redisTemplate.delete(key);
  }

  public void removeRefreshToken(String userId) {
    String key = REFRESH_TOKEN_KEY_PREFIX + userId;
    redisTemplate.delete(key);
  }

  public boolean validateToken(String userId, String token) {
    String storedToken = getToken(userId);

    if (StringUtils.isBlank(storedToken)) {
      return false;
    }
    // 移除 Bearer 前缀后再比较
    String cleanToken = token.startsWith(JWT_HEADER) ? token.substring(JWT_HEADER.length()) : token;
    return storedToken.equals(cleanToken);
  }

  public boolean validateRefreshToken(String userId, String refreshToken) {
    String storedRefreshToken = getRefreshToken(userId);
    if (StringUtils.isBlank(storedRefreshToken)) {
      return false;
    }
    // 移除 Bearer 前缀后再比较
    String cleanToken = refreshToken.startsWith(JWT_HEADER) ? refreshToken.substring(JWT_HEADER.length()) : refreshToken;
    return storedRefreshToken.equals(cleanToken);
  }

  public void clean(String userId) {
    removeToken(userId);
    removeRefreshToken(userId);
  }
}