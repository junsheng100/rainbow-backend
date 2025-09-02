package com.rainbow.base.filter;

import com.rainbow.base.interceptor.XssHttpServletRequestWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XSS过滤器
 */
@Component
public class XssFilter implements Filter {

    /**
     * 排除链接
     */
    private List<String> excludes = new ArrayList<>();

    /**
     * 排除路径正则表达式
     */
    private List<Pattern> excludePatterns = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化排除链接
        initExcludes();

        // 编译排除路径正则表达式
        for (String exclude : excludes) {
            excludePatterns.add(Pattern.compile(exclude, Pattern.CASE_INSENSITIVE));
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        // 如果是排除的路径，则直接放行
        if (isExcluded(req)) {
            chain.doFilter(request, response);
            return;
        }

        // 使用XSS过滤包装类包装请求
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(req);
        chain.doFilter(xssRequest, response);
    }

    @Override
    public void destroy() {
        // 清空排除链接
        excludes.clear();
        excludePatterns.clear();
    }

    /**
     * 初始化排除链接
     */
    private void initExcludes() {
        // 排除静态资源
        excludes.add("/static/.*");
        excludes.add("/assets/.*");
        excludes.add("/css/.*");
        excludes.add("/js/.*");
        excludes.add("/images/.*");
        excludes.add("/fonts/.*");

        // 排除Swagger相关路径
        excludes.add("/swagger-ui.html");
        excludes.add("/swagger-resources/.*");
        excludes.add("/v2/api-docs");
        excludes.add("/webjars/.*");

        // 排除文件上传接口
        excludes.add(".*/upload");

        // 排除富文本编辑器相关接口
        excludes.add(".*/editor/.*");
    }

    /**
     * 判断是否为排除的路径
     */
    private boolean isExcluded(HttpServletRequest request) {
        String path = request.getServletPath();

        for (Pattern pattern : excludePatterns) {
            Matcher matcher = pattern.matcher(path);
            if (matcher.matches()) {
                return true;
            }
        }

        return false;
    }
}