package com.rainbow.user.controller;

import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.exception.NoLoginException;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.domain.LoginUser;
import com.rainbow.system.service.SysResourceService;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.model.UserTotal;
import com.rainbow.user.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理")
public class UserInfoController extends BaseController<UserInfo, String, UserInfoService> {


  @Autowired
  private SysResourceService resourceService;


  @GetMapping(value = "/profile")
  @Operation(summary = "用户信息")
  public Result<UserInfo> getProfile() {
    try {
      if (!isToken()) {
        throw new NoLoginException("未登录");
      }

      // 获取用户信息
      String userId = super.getUserId();
      UserInfo user = service.get(userId);
      user.setPassword("N/A");

      return Result.success(user);
    } catch (Exception e) {
      log.error("Error getting user info", e);
      throw new NoLoginException("获取用户信息失败: " + e.getMessage());
    }
  }

  @OperLog
  @PostMapping(value = "/{userId}/password")
  @Operation(summary = "修改密码")
  public Result<Object> changePassword(@PathVariable(name = "userId") String userId,
                                       @RequestParam String oldPassword,
                                       @RequestParam String newPassword) {
    try {

      if (super.isToken()) {
        // 验证用户ID
        LoginUser loginUser = getLoginUser();
        Assert.notNull(loginUser, "未登录");
        Assert.isTrue(loginUser.getUserId().equals(userId), "用户ID不匹配");

        super.service.changePassword(userId, oldPassword, newPassword);
        log.info("Password changed successfully for user: {}", userId);
        return Result.success();
      }
    } catch (Exception e) {
      log.error("Error changing password for user {}: {}", userId, e.getMessage(), e);
      throw new NoLoginException("修改密码失败: " + e.getMessage());
    }
    return Result.error("修改密码失败");
  }

  @OperLog
  @PutMapping(value = "/{userId}/rest/{password}")
  @Operation(summary = "重置密码")
  public Result<Boolean> restPassword(@PathVariable(name = "userId") String userId,
                                      @PathVariable(name = "password") String password) {
    try {

      boolean isAdmin = service.isAdmin();

      if (isAdmin) {
        Boolean flag = super.service.restPassword(userId, password);
        return Result.success(flag);
      }
      throw new BizException("重置密码失败 ");

    } catch (Exception e) {
      log.error("Error changing password for user {}: {}", userId, e.getMessage(), e);
      throw new NoLoginException("修改密码失败: " + e.getMessage());
    }

  }

  @OperLog
  @Operation(summary = "头像上传")
  @PostMapping("/avatar/upload")
  @ResponseBody
  public Result<String> upload(@RequestPart("file") MultipartFile multipartFile) {
    try {
      LoginUser loginUser = getLoginUser();
      String userId = loginUser.getUserId();
      String srcName = multipartFile.getOriginalFilename();
      String suffix = srcName.substring(srcName.lastIndexOf(ChartEnum.POINT.getCode()) + 1);
      String localPath = "/avatar/" + userId + "." + suffix;

      service.uploadFile(multipartFile, localPath, userId);

      return Result.success(localPath);

    } catch (Exception e) {
      log.error("文件上传失败", e);
      return Result.error("文件上传失败");
    }
  }


  @Operation(summary = "数据统计")
  @GetMapping("/total")
  @ResponseBody
  public Result<UserTotal> total() {
    try {
      UserTotal total = service.total();
      return Result.success(total);

    } catch (Exception e) {
      log.error("数据统计失败", e);
      return Result.error("查询统计数据失败");
    }
  }



}