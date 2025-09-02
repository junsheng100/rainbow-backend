package com.rainbow.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.AddressInfo;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.base.utils.AddressUtils;
import com.rainbow.base.utils.IPUtils;
import com.rainbow.base.utils.JwtTokenUtil;
import com.rainbow.system.entity.SysIPData;
import com.rainbow.system.entity.SysLogin;
import com.rainbow.system.model.vo.LoginData;
import com.rainbow.system.resource.SysIPDataDao;
import com.rainbow.system.resource.SysLoginDao;
import com.rainbow.system.service.SysConfigService;
import com.rainbow.system.service.SysLoginService;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.resource.UserInfoDao;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.rainbow.base.constant.CacheConstants.*;

@Slf4j
@Service
public class SysLoginServiceImpl extends BaseServiceImpl<SysLogin, Long, SysLoginDao> implements SysLoginService {

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private JwtTokenUtil tokenUtil;
  @Autowired
  private UserInfoDao userDao;

  @Autowired
  public RedisTemplate<String, String> redisTemplate;

  @Autowired
  private SysConfigService configService;
  @Autowired
  private SysIPDataDao ipAddressDao;

  @Override
  public SysLogin saveLogin(UserInfo user) {
    if (null == user)
      return null;
    UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

    String userId = user.getUserId();
    String userName = user.getUserName();
    LocalDateTime date = LocalDateTime.now();
    // 获取客户端操作系统
    String os = userAgent.getOperatingSystem().getName();
    // 获取客户端浏览器
    String browser = userAgent.getBrowser().getName();
    // 封装对象
    SysLogin login = new SysLogin();

    login.setType("Login");
    login.setUserId(userId);
    login.setUserName(userName);
    login.setBrowser(browser);
    login.setOs(os);
    login.setOperTime(date);
    login.setFcu(userName);
    login.setFcd(date);
    login.setLcu(userName);
    login.setLcd(date);

    setLogAddress(login);

    super.baseDao.store(login);
    userDao.updateLogin(user.getUserId(), user.getLogin());
    createCacheData(JWT_USERID + userId, login);

    return login;
  }


  @Override
  public SysLogin saveLogout(UserInfo user) {
    if (null == user)
      return null;
    String userId = user.getUserId();
    SysLogin login = getCacheData(JWT_USERID + userId);
    if (null == login)
      return null;
    LocalDateTime date = LocalDateTime.now();
    login.setOperTime(date);
    login.setLcd(date);
    login.setLcu(user.getUserName());
    login.setType("Logout");

    setLogAddress(login);

    super.baseDao.store(login);

    userDao.updateLogout(user.getUserId(), user.getLogout());

    removeCacheData(JWT_USERID + userId);
    return login;
  }


  @Override
  public Boolean cleanAll() {
    return super.baseDao.deleteAll();
  }


  @Transactional
  @Override
  public Boolean saveUserLogin(String userId) {

//    Runnable runnable = new Runnable() {
//      @Override
//      public void run() {
//        UserInfo userInfo = userDao.get(userId);
//        saveLogin(userInfo);
//      }
//    };
//
//    Thread thread = new Thread(runnable);
//    thread.setPriority(Thread.MIN_PRIORITY);
//    thread.start();
    UserInfo userInfo = userDao.get(userId);
    saveLogin(userInfo);

    return true;
  }

  @Transactional
  @Override
  public Boolean saveUserLogout(String userId) {

//    Runnable runnable = new Runnable() {
//      @Override
//      public void run() {
//        UserInfo userInfo = userDao.get(userId);
//        saveLogout(userInfo);
//      }
//    };
//
//    Thread thread = new Thread(runnable);
//    thread.setPriority(Thread.MIN_PRIORITY);
//    thread.start();

    UserInfo userInfo = userDao.get(userId);
    saveLogout(userInfo);

    return true;
  }

  @Override
  public PageData<SysLogin> findOnLine(CommonVo<String> vo) {

    Integer pageNo = null == vo.getPageNo() ? 0 : vo.getPageNo() - 1;
    pageNo = pageNo < 0 ? 0 : pageNo;
    Integer pageSize = null == vo.getPageSize() ? 10 : vo.getPageSize();

    Pageable pageable = PageRequest.of(pageNo, pageSize);
    Long total = 0L;
    List<SysLogin> list = new ArrayList<>();

    Collection<String> userIdList = redisTemplate.keys(TOKEN_KEY_PREFIX + "*");

    if (CollectionUtils.isNotEmpty(userIdList)) {

      for (String key : userIdList) {
        String userId = key.replace(TOKEN_KEY_PREFIX, "");
        SysLogin login = getCacheData(JWT_USERID + userId);
        list.add(login);
      }

      String keyword = vo.getData();
      if (StringUtils.isNotEmpty(keyword)) {
        list = list.stream().filter(t -> {
          return isContains(t, keyword);
        }).collect(Collectors.toList());
      }

      if (CollectionUtils.isNotEmpty(list)) {
        total = list.stream().count();
        Long skip = pageSize * (pageNo - 1) * 1L;
        skip = skip < 0 ? 0L : skip;
        Long limit = pageSize * 1L;
        list = list.stream().skip(skip).limit(limit).collect(Collectors.toList());
      }
    }
    Page<SysLogin> page = new PageImpl<>(list, pageable, total);

    return new PageData<>(page);
  }

  private boolean isContains(SysLogin data, String keyword) {
    if (null == data)
      return false;
    if (data.getUserName().contains(keyword))
      return true;
    if (data.getIpaddr().contains(keyword))
      return true;
    if (data.getLoginLocation().contains(keyword))
      return true;
    if (data.getBrowser().contains(keyword))
      return true;
    if (data.getOs().contains(keyword))
      return true;
    if (null != data.getOperTime() && data.getOperTime().toString().contains(keyword)) {
      return true;
    }

    return false;
  }

  @Transactional
  @Override
  public Boolean logout(String... userIdArray) {
    Assert.notNull(userIdArray, "用户ID不能为空");
    List<UserInfo> userInfoList = userDao.findInUserId(Arrays.asList(userIdArray));

    if (CollectionUtils.isNotEmpty(userInfoList)) {
      for (UserInfo userInfo : userInfoList) {
        saveLogout(userInfo);
        String id = userInfo.getUserId();

        removeCacheData(JWT_USERID + id);
        removeCacheData(TOKEN_KEY_PREFIX + id);
        removeCacheData(REFRESH_TOKEN_KEY_PREFIX + id);
      }
    }
    return true;
  }

  @Override
  public Boolean logoutAll() {
    Collection<String> userIdList = redisTemplate.keys(TOKEN_KEY_PREFIX + "*");
    List<SysLogin> list = new ArrayList<>();

    if (CollectionUtils.isNotEmpty(userIdList)) {
      list = userIdList.stream().map(userId -> {
        SysLogin login = getCacheData(userId);
        return login;
      }).filter(t -> null != t).collect(Collectors.toList());

      if (CollectionUtils.isNotEmpty(list)) {
        LocalDateTime date = LocalDateTime.now();
        for (SysLogin login : list) {
          login.setOperTime(date);
          baseDao.store(login);
          String userId = login.getUserId();
          // 更新用户登录时间
          userDao.updateLogout(userId, date);
          // 删除缓存

          removeCacheData(JWT_USERID + userId);
          removeCacheData(TOKEN_KEY_PREFIX + userId);
          removeCacheData(REFRESH_TOKEN_KEY_PREFIX + userId);
        }
      }
    }


    return true;
  }

  @Override
  public List<LoginData> totalAreaPro() {
    List<LoginData> list = new ArrayList<>();
    List<Object[]> dataList = baseDao.totalAreaPro();

    if (CollectionUtils.isNotEmpty(dataList)) {
      dataList.stream().forEach(data -> {

        String country = data[0].toString();
        String pro = data[1].toString();
        String city = data[2].toString();
        Long total = data[3].toString() == null ? 0L : Long.parseLong(data[3].toString());

        LoginData loginData = new LoginData(country, pro, city, total);
        list.add(loginData);
      });
    }

    return list;
  }

  @Override
  public Boolean delete(Long id) {
    return baseDao.remove(id);
  }

  private void createCacheData(String key, SysLogin data) {
    redisTemplate.opsForValue().set(key, JSON.toJSONString(data));
  }

  private SysLogin getCacheData(String key) {
    String data = redisTemplate.opsForValue().get(key);
    return JSON.parseObject(data, SysLogin.class);
  }

  private void removeCacheData(String key) {
    redisTemplate.delete(key);
  }


  private SysIPData setLogAddress(SysLogin login) {
    if (null == login)
      return null;
    String ip = IPUtils.getIpAddr(request);

    if (IPUtils.internalIp(ip))
      ip = AddressUtils.getIpAddress();
    SysIPData ipData = ipAddressDao.getByIpaddr(ip);

    if (null == ipData) {
      AddressInfo addressInfo = AddressUtils.getRealAddressByIP(ip);
      login.setIpaddr(addressInfo.getIp());
      login.setLoginLocation(addressInfo.getAddr());
      login.setCountry(addressInfo.getCountry());
      login.setPro(addressInfo.getPro());
      login.setCity(addressInfo.getCity());
      /// ///////////////////////////
      ipData = new SysIPData(addressInfo);
      ipAddressDao.store(ipData);
    }else{
      login.setIpaddr(ipData.getIpaddr());
      login.setLoginLocation(ipData.getLocation());
      login.setCountry(ipData.getCountry());
      login.setPro(ipData.getPro());
      login.setCity(ipData.getCity());
    }

    return ipData;
  }
}
