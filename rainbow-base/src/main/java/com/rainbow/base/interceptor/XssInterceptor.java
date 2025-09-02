package com.rainbow.base.interceptor;

import com.rainbow.base.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * XSS攻击拦截器
 */
@Component
public class XssInterceptor implements HandlerInterceptor {

    /**
     * XSS攻击特征正则表达式
     */
    private static final Pattern[] XSS_PATTERNS = {
            // 避免script标签
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            // 避免eval(...)等JavaScript执行方法
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            // 避免javascript:伪协议
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),

            // 避免onload等事件属性
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("onerror(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("onclick(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("onmouseover(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查请求参数是否包含XSS攻击特征
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String[] values = request.getParameterValues(name);
            if (values != null) {
                for (String value : values) {
                    if (StringUtils.isNotBlank(value) && containsXss(value)) {
                        throw new BizException("请求中包含非法字符，可能存在XSS攻击风险");
                    }
                }
            }
        }

        // 检查请求头是否包含XSS攻击特征
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            // 排除一些常见的安全头部
            if (!"referer".equalsIgnoreCase(header) &&
                !"user-agent".equalsIgnoreCase(header) &&
                !"origin".equalsIgnoreCase(header)) {
                String value = request.getHeader(header);
                if (StringUtils.isNotBlank(value) && containsXss(value)) {
                    throw new BizException("请求头中包含非法字符，可能存在XSS攻击风险");
                }
            }
        }

        return true;
    }

    /**
     * 检查字符串是否包含XSS攻击特征
     */
    private boolean containsXss(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }

        // 使用正则表达式检查是否包含XSS攻击特征
        for (Pattern pattern : XSS_PATTERNS) {
            if (pattern.matcher(value).find()) {
                return true;
            }
        }

        return false;
    }
}
