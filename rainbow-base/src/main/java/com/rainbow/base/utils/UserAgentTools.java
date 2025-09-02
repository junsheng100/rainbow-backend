package com.rainbow.base.utils;

import com.rainbow.base.enums.ClientType;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;

import static com.rainbow.base.constant.DataConstant.SERVER_REQUEST_HEADER;
import static com.rainbow.base.constant.DataConstant.SERVER_REQUEST_HEADER_VALUE;

/**
 * User-Agent工具类，用于判断客户端类型
 */
public class UserAgentTools {
    
    
    /**
     * 根据User-Agent字符串判断客户端类型
     * 
     * @param userAgent User-Agent字符串
     * @return 客户端类型
     */
    public static ClientType getClientType(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return ClientType.UNKNOWN;
        }
        
        // 使用UserAgentUtils库解析User-Agent
        UserAgent agent = UserAgent.parseUserAgentString(userAgent);
        OperatingSystem os = agent.getOperatingSystem();
        DeviceType deviceType = os.getDeviceType();
        
        // 判断是否为微信小程序
        if (userAgent.toLowerCase().contains("miniprogram") || userAgent.toLowerCase().contains("wechatdevtools")) {
            return ClientType.WECHAT_MINI_PROGRAM;
        }
        
        // 根据设备类型判断客户端
        switch (deviceType) {
            case MOBILE:
            case TABLET:
                // 进一步判断是否为APP
                if (userAgent.toLowerCase().contains("app")) {
                    if (os.getName().toLowerCase().contains("android")) {
                        return ClientType.ANDROID_APP;
                    } else if (os.getName().toLowerCase().contains("ios")) {
                        return ClientType.IOS_APP;
                    }
                }
                return ClientType.H5;
            case COMPUTER:
                return ClientType.WEB;
            default:
                return ClientType.UNKNOWN;
        }
    }
    
    /**
     * 从HttpServletRequest中获取User-Agent并判断客户端类型
     * 
     * @param request HttpServletRequest对象
     * @return 客户端类型
     */
    public static ClientType getClientType(HttpServletRequest request) {
        if (request == null) {
            return ClientType.UNKNOWN;
        }
        String REQUEST_HEADER_VALUE = request.getHeader(SERVER_REQUEST_HEADER);
        if(StringUtils.isNotBlank(REQUEST_HEADER_VALUE)){
            if(SERVER_REQUEST_HEADER_VALUE.equals(REQUEST_HEADER_VALUE)){
                return ClientType.SERVER;
            }
        }
        
        String userAgent = request.getHeader("User-Agent");
        return getClientType(userAgent);
    }
    
    /**
     * 判断是否为Web浏览器
     * 
     * @param userAgent User-Agent字符串
     * @return 是否为Web浏览器
     */
    public static boolean isWeb(String userAgent) {
        return getClientType(userAgent) == ClientType.WEB;
    }
    
    /**
     * 判断是否为H5页面
     * 
     * @param userAgent User-Agent字符串
     * @return 是否为H5页面
     */
    public static boolean isH5(String userAgent) {
        return getClientType(userAgent) == ClientType.H5;
    }
    
    /**
     * 判断是否为Android应用
     * 
     * @param userAgent User-Agent字符串
     * @return 是否为Android应用
     */
    public static boolean isAndroidApp(String userAgent) {
        return getClientType(userAgent) == ClientType.ANDROID_APP;
    }
    
    /**
     * 判断是否为iOS应用
     * 
     * @param userAgent User-Agent字符串
     * @return 是否为iOS应用
     */
    public static boolean isIosApp(String userAgent) {
        return getClientType(userAgent) == ClientType.IOS_APP;
    }
    
    /**
     * 判断是否为微信小程序
     * 
     * @param userAgent User-Agent字符串
     * @return 是否为微信小程序
     */
    public static boolean isWechatMiniProgram(String userAgent) {
        return getClientType(userAgent) == ClientType.WECHAT_MINI_PROGRAM;
    }
    
    /**
     * 从HttpServletRequest中判断是否为Web浏览器
     * 
     * @param request HttpServletRequest对象
     * @return 是否为Web浏览器
     */
    public static boolean isWeb(HttpServletRequest request) {
        return getClientType(request) == ClientType.WEB;
    }
    
    /**
     * 从HttpServletRequest中判断是否为H5页面
     * 
     * @param request HttpServletRequest对象
     * @return 是否为H5页面
     */
    public static boolean isH5(HttpServletRequest request) {
        return getClientType(request) == ClientType.H5;
    }
    
    /**
     * 从HttpServletRequest中判断是否为Android应用
     * 
     * @param request HttpServletRequest对象
     * @return 是否为Android应用
     */
    public static boolean isAndroidApp(HttpServletRequest request) {
        return getClientType(request) == ClientType.ANDROID_APP;
    }
    
    /**
     * 从HttpServletRequest中判断是否为iOS应用
     * 
     * @param request HttpServletRequest对象
     * @return 是否为iOS应用
     */
    public static boolean isIosApp(HttpServletRequest request) {
        return getClientType(request) == ClientType.IOS_APP;
    }
    
    /**
     * 从HttpServletRequest中判断是否为微信小程序
     * 
     * @param request HttpServletRequest对象
     * @return 是否为微信小程序
     */
    public static boolean isWechatMiniProgram(HttpServletRequest request) {
        return getClientType(request) == ClientType.WECHAT_MINI_PROGRAM;
    }
    
    /**
     * 从HttpServletRequest中判断是否为服务器端请求
     * 
     * @param request HttpServletRequest对象
     * @return 是否为服务器端请求
     */
    public static boolean isServer(HttpServletRequest request) {
        return getClientType(request) == ClientType.UNKNOWN;
    }
    
    /**
     * 根据User-Agent字符串判断是否为服务器端请求
     * 
     * @param userAgent User-Agent字符串
     * @return 是否为服务器端请求
     */
    public static boolean isServer(String userAgent) {
        return getClientType(userAgent) == ClientType.UNKNOWN;
    }
}