package com.rainbow.user.service.impl;

import com.rainbow.base.constant.CacheConstants;
import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.enums.UserType;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.exception.DataException;
import com.rainbow.base.exception.NoLoginException;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.system.resource.SysConfigDao;
import com.rainbow.system.resource.SysLoginDao;
import com.rainbow.user.entity.*;
import com.rainbow.user.model.UserTotal;
import com.rainbow.user.resource.*;
import com.rainbow.user.service.UserInfoService;
import com.rainbow.user.utils.PasswordValidator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo, String, UserInfoDao> implements UserInfoService {


  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private PasswordValidator passwordValidator;
  @Autowired
  private DeptInfoDao deptInfoDao;
  @Autowired
  private UserPostDao userPostDao;
  @Autowired
  private UserRoleDao userRoleDao;
  @Autowired
  private SysLoginDao sysLoingDao;
  @Autowired
  private SysConfigDao configDao;
  @Autowired
  private UserPasswdDao passwdDao;
  @Autowired
  private SysRoleMenuDao roleMenuDao;
  @Autowired
  private SysMenuDao menuDao;
  @Autowired
  private SysRoleDao roleDao;


  @Autowired
  public RedisTemplate<String, String> redisTemplate;


  @Override
  public UserInfo store(@Valid UserInfo entity) {
    String password = entity.getPassword();

    if (StringUtils.isBlank(entity.getUserId())) {
      entity = createUser(entity);
    } else {
      if (StringUtils.isNotBlank(password)) {
        password = passwdDao.getPassword(password);
        UserPasswd passwd = new UserPasswd(entity.getUserId(), password);
        passwdDao.store(passwd);
      }
      baseDao.store(entity);
    }

    if (CollectionUtils.isNotEmpty(entity.getPostIdList())) {
      userPostDao.storeUserInfo(entity);
    }
    if (CollectionUtils.isNotEmpty(entity.getRoleIdList())) {
      userRoleDao.storeUserInfo(entity);
    }

    return entity;
  }


  @PreAuthorize("hasRole('ADMIN')")
  @Override
  public UserInfo createUser(UserInfo userInfo) {
    try {
      // 验证用户名是否存在
      if (baseDao.existsByUserName(userInfo.getUserName())) {
        throw new DataException("用户名已存在");
      }

      // 验证用户名是否存在
      if (baseDao.existsByUserName(userInfo.getUserName())) {
        throw new DataException("用户名已存在");
      }
      userInfo.setStatus("0");
      baseDao.store(userInfo);

      String password = userInfo.getPassword();
      if (StringUtils.isNotBlank(password)) {
        // 验证密码强度
        passwordValidator.validate(userInfo.getPassword());
        // 加密密码
        password = passwdDao.getPassword(userInfo.getPassword());
        UserPasswd passwd = new UserPasswd(userInfo.getUserId(), password);
        passwd.setStatus(UseStatus.NO.getCode());
        passwdDao.store(passwd);
      }
    } catch (Exception e) {
      log.error("创建用户失败", e);
      throw new BizException(e.getMessage());
    }

    return userInfo;
  }

  //    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
  @Override
  public UserInfo updateUser(String userId, UserInfo userInfo) {
    UserInfo existingUser = get(userId);

    // 更新用户信息
    existingUser.setNickname(userInfo.getNickname());
    existingUser.setUserType(userInfo.getUserType());

    return baseDao.store(existingUser);
  }


  @Override
  public UserInfo findByUserName(String userName) {
    return baseDao.findByUserName(userName);

  }

  @Override
  public void changePassword(String userId, String oldPassword, String newPassword) {

    UserPasswd user = passwdDao.getByUserId(userId);
    // 验证旧密码
    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new NoLoginException("旧密码错误");
    }

    // 验证新密码强度
    passwordValidator.validate(newPassword);

    // 更新密码
    String password = passwdDao.getPassword(newPassword);
    UserPasswd passwd = new UserPasswd(userId, password);
    passwd.setStatus(UseStatus.NO.getCode());
    passwdDao.store(passwd);
  }

  @Override
  public Boolean restPassword(String userId, String password) {
    UserInfo user = baseDao.get(userId);
    if (null == user)
      throw new NoLoginException("用户不存在");
    // 验证新密码强度
    passwordValidator.validate(password);
    password = passwdDao.getPassword(password);
    UserPasswd passwd = new UserPasswd(userId, password);
    passwdDao.store(passwd);
    return true;
  }

  @Override
  public UserTotal total() {
    UserTotal data = new UserTotal();
    List<UserInfo> userList = baseDao.findUserAll();
    String theDay = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    Long total = 0L; // 总用户数
    Long login = sysLoingDao.countLogin(theDay); // 今日登录
    Long online = 0L; // 在线用户
    if (CollectionUtils.isNotEmpty(userList)) {
      Collection<String> onLineKeys = redisTemplate.keys(CacheConstants.TOKEN_KEY_PREFIX + "*");
      online = CollectionUtils.isEmpty(onLineKeys) ? 0L : onLineKeys.stream().count();
      total = userList.stream().count();
    }

    data.setTotal(total);
    data.setLogin(login);
    data.setOnline(online);
    return data;
  }

  @Override
  public String uploadFile(MultipartFile multipartFile, String localPath, String userId) {
    File file = null;
    try {
      String filePath = configDao.getFileBasePath() + localPath;
      file = new File(filePath);
      File fdir = file.getParentFile();
      if (!fdir.exists())
        fdir.mkdirs();
      multipartFile.transferTo(file);

      UserInfo userInfo = get(userId);
      userInfo.setAvatar(localPath);
      baseDao.store(userInfo);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return localPath;
  }


  @SneakyThrows
  @Override
  public List<String> getUserRoles(String userId) {
    List<String> list = new ArrayList<>();
    boolean isAdmin = baseDao.isUserType(UserType.ADMIN.name(), userId);
    if (isAdmin) {
      list.add(UserType.ADMIN.name().toLowerCase());
      return list;
    }

    List<UserRole> userRoles = userRoleDao.findByUserId(userId);

    if (CollectionUtils.isEmpty(userRoles))
      throw new NoLoginException("未授权");
    List<Long> roleIdList = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
    List<SysRole> roleList = roleDao.findInId(roleIdList);
    if (CollectionUtils.isEmpty(roleList)) {
      throw new NoLoginException("未授权");
    }

    list = roleList.stream().map(SysRole::getRoleKey).distinct().collect(Collectors.toList());

    return list;
  }

  @Override
  public List<String> getUserPermissions(String userId) {
    List<String> list = new ArrayList<>();
    boolean isAdmin = baseDao.isUserType(UserType.ADMIN.name(), userId);
    if (isAdmin) {
      list.add("*:*:*");
      return list;
    }

    list = menuDao.findPermsByUserId(userId);
    if (CollectionUtils.isEmpty(list))
      throw new NoLoginException("未授权");
    return list;
  }


  public void convertData(UserInfo entity) {
    if (null != entity) {
      String userId = entity.getUserId();
      Long deptId = entity.getDeptId();
      DeptInfo deptInfo = null == deptId ? null : deptInfoDao.get(deptId);
      entity.setDeptName(null == deptInfo ? "" : deptInfo.getDeptName());

      Map<String, List<UserPost>> postMp = userPostDao.findUserPostList(Arrays.asList(userId));
      Map<String, List<UserRole>> roleMp = userRoleDao.findUseRoleList(Arrays.asList(userId));

      convertUserInfo(entity, postMp, roleMp);

    }
  }

  public void convertCollection(List<UserInfo> list) {
    if (CollectionUtils.isEmpty(list))
      return;
    List<String> userIdList = list.stream().map(UserInfo::getUserId).collect(Collectors.toList());
    List<Long> deptIdList = list.stream().map(UserInfo::getDeptId).collect(Collectors.toList());
    Map<Long, String> deptMp = deptInfoDao.findDeptName(deptIdList);
    Map<String, List<UserPost>> postMp = userPostDao.findUserPostList(userIdList);
    Map<String, List<UserRole>> roleMp = userRoleDao.findUseRoleList(userIdList);

    list.stream().forEach(t -> {
      String deptName = MapUtils.getString(deptMp, t.getDeptId());
      t.setDeptName(deptName);
      convertUserInfo(t, postMp, roleMp);
    });


  }

  public void convertUserInfo(UserInfo t, Map<String, List<UserPost>> postMp, Map<String, List<UserRole>> roleMp) {
    List<UserPost> postList = (List<UserPost>) MapUtils.getObject(postMp, t.getUserId(), null);
    if (CollectionUtils.isNotEmpty(postList)) {
      List<Long> postIdList = postList.stream().map(UserPost::getPostId).collect(Collectors.toList());
      List<String> postNameList = postList.stream().map(UserPost::getPostName).collect(Collectors.toList());
      t.setPostIdList(postIdList);
      t.setPostNameList(postNameList);
    }
    List<UserRole> roleList = (List<UserRole>) MapUtils.getObject(roleMp, t.getUserId(), null);
    if (CollectionUtils.isNotEmpty(roleList)) {
      List<Long> roleIdList = roleList.stream().map(UserRole::getRoleId).distinct().collect(Collectors.toList());
      List<String> roleNameList = roleList.stream().map(UserRole::getRoleName).distinct().collect(Collectors.toList());
      t.setRoleIdList(roleIdList);
      t.setRoleNameList(roleNameList);
    }
    t.setPassword(null);
  }
} 