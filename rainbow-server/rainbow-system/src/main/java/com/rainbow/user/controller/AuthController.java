package com.rainbow.user.controller;

import com.rainbow.base.config.JwtConfig;
import com.rainbow.base.config.RedisTokenStore;
import com.rainbow.base.exception.NoLoginException;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.domain.LoginUser;
import com.rainbow.base.model.router.RouterVo;
import com.rainbow.base.utils.CommonUtils;
import com.rainbow.base.utils.JwtTokenUtil;
import com.rainbow.system.service.SysLoginService;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.entity.UserPasswd;
import com.rainbow.user.model.LoginRequest;
import com.rainbow.user.model.LoginResponse;
import com.rainbow.user.service.AuthService;
import com.rainbow.user.service.RouterService;
import com.rainbow.user.service.UserInfoService;
import com.rainbow.user.service.UserPasswdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理")
public class AuthController {

  @Autowired
  private UserInfoService userInfoService;
  @Autowired
  private UserPasswdService passwdService;
  @Autowired
  private RouterService routerService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private JwtConfig config;

  @Autowired
  private RedisTokenStore redisTokenStore;

  @Autowired
  private SysLoginService loginService;

  @Autowired
  private AuthService authService;



  @PostMapping("/login")
  @Operation(summary = "用户登录")
  public Result<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
    // 查找用户
    UserInfo user = userInfoService.findByUserName(request.getUserName());

    if (user == null) {
      throw new NoLoginException("用户名或密码错误");
    }

    UserPasswd userPasswd = passwdService.getByUserId(user.getUserId());
    if (null == userPasswd) {
      throw new NoLoginException("用户名或密码错误");
    }

    // 验证密码
    if (!passwordEncoder.matches(request.getPassword(), userPasswd.getPassword())) {
      throw new NoLoginException("用户名或密码错误");
    }
    LoginUser loginUser = new LoginUser();
    BeanUtils.copyProperties(user, loginUser, CommonUtils.getNullPropertyNames(user));


    JwtConfig jwtConfig = authService.getJwtConfig();
    jwtTokenUtil.setConfig(jwtConfig);
    // 生成token
    String accessToken = jwtTokenUtil.generateAccessToken(loginUser);
    String refreshToken = jwtTokenUtil.generateRefreshToken(loginUser);

    // 构建响应
    LoginResponse response = new LoginResponse();
    BeanUtils.copyProperties(user, response, CommonUtils.getNullPropertyNames(user));
    response.setAccessToken(accessToken);
    response.setRefreshToken(refreshToken);
    response.setExpiresIn(jwtTokenUtil.getAccessTokenValidityInSeconds());


    loginService.saveUserLogin(user.getUserId());
    return Result.success(response);
  }

  @PostMapping("/refresh")
  @Operation(summary = "刷新令牌")
  public Result<LoginResponse> refreshToken(@RequestHeader("Authorization") String refreshToken) {
    try {
      // 验证刷新令牌
      if (!jwtTokenUtil.validateToken(refreshToken)) {
        String token = jwtTokenUtil.cleanToken(refreshToken);
        if (token != null) {
          String userId = jwtTokenUtil.getUserIdFromToken(token);
          redisTokenStore.clean(userId);
        }
        throw new NoLoginException("无效的刷新令牌");
      }

      // 获取用户信息
      String userId = jwtTokenUtil.getUserIdFromToken(refreshToken);
      UserInfo user = userInfoService.get(userId);
      LoginUser loginUser = new LoginUser();
      BeanUtils.copyProperties(user, loginUser, CommonUtils.getNullPropertyNames(user));
      // 生成新的访问令牌
      String newAccessToken = jwtTokenUtil.generateAccessToken(loginUser);
      String newRefreshToken = jwtTokenUtil.generateRefreshToken(loginUser);

      // 构建响应
      LoginResponse response = new LoginResponse();
      BeanUtils.copyProperties(user, response, CommonUtils.getNullPropertyNames(user));
      response.setAccessToken(newAccessToken);
      response.setRefreshToken(newRefreshToken);
      response.setExpiresIn(jwtTokenUtil.getAccessTokenValidityInSeconds());

      return Result.success(response);
    } catch (Exception e) {
      log.error("Error refreshing token", e);
      throw new NoLoginException("刷新令牌失败: " + e.getMessage());
    }
  }


  @GetMapping("/validate")
  @Operation(summary = "验证令牌")
  public Result<Boolean> validateToken(@RequestHeader("Authorization") String token) {
    return Result.success(jwtTokenUtil.validateToken(token));
  }


  @GetMapping("/info")
  @Operation(summary = "获取用户信息")
  public Result<LoginUser> getInfo() {
    try {
      String token = request.getHeader(config.getHeader());
      // 使用统一的方法处理 token
      token = jwtTokenUtil.cleanToken(token);
      if (token == null) {
//        throw new NoLoginException("无效的令牌");
        throw new NoLoginException("无效的令牌");
      }
      // 验证令牌
      if (!jwtTokenUtil.validateToken(token)) {
        throw new NoLoginException("无效的令牌");
      }
      // 获取用户信息
      String userId = jwtTokenUtil.getUserIdFromToken(token);
      UserInfo user = userInfoService.get(userId);
      user.setPassword("N/A");
      LoginUser loginUser = new LoginUser();
      BeanUtils.copyProperties(user, loginUser, CommonUtils.getNullPropertyNames(user));

      List<String> roles = userInfoService.getUserRoles(userId);

      List<String> permissions = userInfoService.getUserPermissions(userId);

      loginUser.setRoles(roles);
      loginUser.setPermissions(permissions);

      return Result.success(loginUser);
    } catch (Exception e) {
      log.error("Error getting user info", e);
      throw new NoLoginException("获取用户信息失败: " + e.getMessage());
    }
  }


  /**
   * 获取路由信息
   *
   * @return 路由信息
   */
  @GetMapping("/routers")
  @Operation(description = "获取路由数据")
  public Result<List<RouterVo>> getRouters() {
    List<RouterVo> menus = routerService.getRouters();
    return Result.success(menus);
  }


  @PostMapping("/logout")
  @Operation(summary = "用户登出")
  public Result<Map> logout() {
    Map result = new HashMap();
    String userId = null;
    try {
      String token = request.getHeader(config.getHeader());
      // 使用统一的方法处理 token
      token = jwtTokenUtil.cleanToken(token);
      if (token != null) {
        userId = jwtTokenUtil.getUserIdFromToken(token);

        loginService.saveUserLogout(userId);
        redisTokenStore.clean(userId);
      }
    } catch (Exception e) {
      if (StringUtils.isNotBlank(userId)) {
        redisTokenStore.clean(userId);
      }
    }
    return Result.success(new HashMap());
  }



} 