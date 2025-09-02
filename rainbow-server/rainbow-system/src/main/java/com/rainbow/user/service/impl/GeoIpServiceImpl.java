package com.rainbow.user.service.impl;//package com.rainbow.user.service.impl;
//
//import cn.hutool.core.util.CharsetUtil;
//import cn.hutool.core.util.PageUtil;
//import cn.hutool.http.HttpUtil;
//import com.alibaba.fastjson2.JSON;
//import com.rainbow.base.model.vo.AddressInfo;
//import com.rainbow.user.service.GeoIpService;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * @Author：QQ:304299340
// * @Package：com.rainbow.user.service.impl
// * @Filename：GeoIpServiceImpl
// * @Date：2025/8/24 19:15
// * @Describe:
// */
//@Service
//public class GeoIpServiceImpl implements GeoIpService {
//
//  @Autowired
//  private HttpServletRequest request;
//
//  public static String IP4_URL = "https://4.ipconfig.com/";
//
//  public static String IP_ADDRESS_URL = "http://whois.pconline.com.cn/ipJson.jsp?json=true";
//
//  @Override
//  public String getIp() {
//
//    try {
//      String ip = HttpUtil.get(IP4_URL, CharsetUtil.CHARSET_UTF_8);
//      if (StringUtils.isNotBlank(ip)) {
//        ip = ip.split("%")[0];
//        return ip;
//      }
//    } catch (Exception e) {
//      return null;
//    }
//    return null;
//  }
//
//  @Override
//  public AddressInfo getAddress(String ip) {
//
//    try {
//      String result = HttpUtil.get(IP_ADDRESS_URL, CharsetUtil.CHARSET_UTF_8);
//      if (StringUtils.isNotBlank(result)) {
//        AddressInfo addressInfo = JSON.parseObject(result, AddressInfo.class);
//        return addressInfo;
//      }
//    } catch (Exception e) {
//      return null;
//    }
//    return null;
//  }
//}
