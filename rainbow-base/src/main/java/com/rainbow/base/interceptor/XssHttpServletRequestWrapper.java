package com.rainbow.base.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * XSS过滤处理包装类
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return cleanXss(value);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        int length = values.length;
        String[] escapeValues = new String[length];
        for (int i = 0; i < length; i++) {
            escapeValues[i] = cleanXss(values[i]);
        }
        return escapeValues;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = super.getParameterMap();
        Map<String, String[]> cleanMap = new LinkedHashMap<>();
        if (map != null) {
            for (Map.Entry<String, String[]> entry : map.entrySet()) {
                String[] values = entry.getValue();
                if (values != null) {
                    String[] cleanValues = new String[values.length];
                    for (int i = 0; i < values.length; i++) {
                        cleanValues[i] = cleanXss(values[i]);
                    }
                    cleanMap.put(entry.getKey(), cleanValues);
                }
            }
        }
        return cleanMap;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return cleanXss(value);
    }

    /**
     * 清除XSS攻击字符
     */
    private String cleanXss(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }

        // 使用 commons-text 提供的 HTML 转义方法
        value = StringEscapeUtils.escapeHtml4(value);

        // 过滤特殊字符
        value = value.replaceAll("\\(", "&#40;")
                .replaceAll("\\)", "&#41;")
                .replaceAll("'", "&#39;")
                .replaceAll("eval\\((.*)\\)", "")
                .replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"")
                .replaceAll("script", "");

        return value;
    }
}
