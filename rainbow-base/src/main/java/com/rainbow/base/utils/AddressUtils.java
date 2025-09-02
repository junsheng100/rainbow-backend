package com.rainbow.base.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rainbow.base.model.vo.AddressInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取地址类
 *
 * @author rainvom
 */
public class AddressUtils {
  public static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

  // IP地址查询
  public static String IP4_URL = "https://ifconfig.io/all.json";
  public static String IP4_SIMPLE = "https://4.ipconfig.com/";
  public static String IP_ADDRESS_URL = "http://whois.pconline.com.cn/ipJson.jsp";

  // 未知地址
  public static final String UNKNOWN = "N/A";

  public static AddressInfo getRealAddressByIP(String ip) {
    try {
      JSONObject jsonObject = null;
      if (IPUtils.internalIp(ip)) {
        jsonObject = getIp4Address();
        ip = jsonObject.getString("ip");

      }
      if (StringUtils.isBlank(ip))
        return null;
      String apiUrl = IP_ADDRESS_URL + "?" + "ip=" + ip + "&json=true";
      String rspStr = HttpUtil.get(apiUrl, CharsetUtil.CHARSET_UTF_8);

      if (StringUtils.isEmpty(rspStr)) {
        log.error("获取地理位置异常 {}", ip);
        return null;
      }
      AddressInfo addressInfo = JSON.parseObject(rspStr, AddressInfo.class);

      JSONObject json = getIp4Address();
      if(null != json){
        String country = json.getString("country_code");
        addressInfo.setCountry(country);
      }

      return addressInfo;
    } catch (Exception e) {
      log.error("获取地理位置异常 {}", ip);
    }

    return null;
  }


  public static String getIpAddress() {
    String ip = null;

    try {
      ip = IPUtils.getHostIp();
      if (!IPUtils.internalIp(ip)) {
        return ip;
      }
      ip = HttpUtil.get(IP4_SIMPLE, CharsetUtil.CHARSET_UTF_8);
      if (StringUtils.isNotBlank(ip)) {
        ip = ip.split("%")[0];
      }
    } catch (Exception e) {
      return null;
    }
    return ip;
  }

  public static JSONObject getIp4Address() {
    try {
      String json = HttpUtil.get(IP4_URL, CharsetUtil.CHARSET_UTF_8);
      return JSON.parseObject(json);
    } catch (Exception e) {
      return null;
    }
  }

}
