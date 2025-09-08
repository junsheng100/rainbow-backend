package com.rainbow.user.service;

import com.rainbow.base.config.JwtConfig;
import com.rainbow.user.entity.UserInfo;

import javax.validation.constraints.NotBlank;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.user.service
 * @Filename：AuthService
 * @Date：2025/9/8 10:58
 * @Describe:
 */
public interface AuthService {

   JwtConfig getJwtConfig();

}
