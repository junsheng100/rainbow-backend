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
 * SQL注入拦截器
 */
@Component
public class SqlInjectionInterceptor implements HandlerInterceptor {

    /**
     * SQL注入检测正则表达式
     */
    private static final Pattern[] SQL_INJECTION_PATTERNS = {
            // 检测常见的SQL注入特征
            Pattern.compile("(?i)(.*)(\\b)+(OR|AND)(\\b)+(.*)=(.*)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?i).*\\b(AND|OR)\\b.*(UNION|SELECT|INSERT|DELETE|UPDATE|DROP|TRUNCATE|ALTER)\\b.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?i).*\\b(UNION|SELECT|INSERT|DELETE|UPDATE|DROP|TRUNCATE|ALTER)\\b.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?i).*['\\\"][\\s]*(OR|AND)[\\s]*['\\\"].*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?i).*['\\\"][\\s]*(UNION|SELECT|INSERT|DELETE|UPDATE|DROP|TRUNCATE|ALTER)[\\s]*['\\\"].*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?i).*['\\\"][\\s]*(AND|OR)[\\s]*[=><].*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?i).*['\\\"][\\s]*(AND|OR)[\\s]*[0-9]+=.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?i).*['\\\"][\\s]*(AND|OR)[\\s]*['\\\"][\\s]*=.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?i).*\\b(SLEEP|BENCHMARK|WAITFOR|DELAY)\\b.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?i).*\\b(XP_CMDSHELL|SP_EXECUTESQL)\\b.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?i).*\\b(INFORMATION_SCHEMA|SYSIBM|SYSDUMMY1)\\b.*", Pattern.CASE_INSENSITIVE)
    };

    /**
     * SQL注入风险字符
     */
    private static final String[] SQL_INJECTION_CHARS = {
            "';", "--;", "/*", "*/", "@@", "@",
            "char ", "nchar ",
            "varchar ", "nvarchar ",
            "alter ", "begin ", "cast ", "create ", "cursor ", "declare ", "delete ", "drop ",
            "end ", "exec ", "execute ", "fetch ", "insert ", "kill ", "select ", "sys ",
            "sysobjects ", "syscolumns ", "table ", "update "
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查请求参数是否包含SQL注入特征
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String[] values = request.getParameterValues(name);
            if (values != null) {
                for (String value : values) {
                    if (StringUtils.isNotBlank(value)) {
                        checkSqlInjection(name, value);
                    }
                }
            }
        }

        // 检查请求头是否包含SQL注入特征
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            // 排除一些常见的安全头部
            if (!"referer".equalsIgnoreCase(header) &&
                !"user-agent".equalsIgnoreCase(header) &&
                !"origin".equalsIgnoreCase(header)) {
                String value = request.getHeader(header);
                if (StringUtils.isNotBlank(value)) {
                    checkSqlInjection(header, value);
                }
            }
        }

        return true;
    }

    /**
     * 检查是否存在SQL注入
     */
    private void checkSqlInjection(String paramName, String value) {
        if (StringUtils.isBlank(value)) {
            return;
        }

        // 检查是否包含SQL注入特征
        for (Pattern pattern : SQL_INJECTION_PATTERNS) {
            if (pattern.matcher(value).matches()) {
                String errorMessage = String.format("参数[%s]中包含非法字符，可能存在SQL注入风险: %s", paramName, value);
                throw new BizException(errorMessage);
            }
        }

        // 检查是否包含特殊字符
        String lowerValue = value.toLowerCase();
        for (String sqlInjectionChar : SQL_INJECTION_CHARS) {
            if (lowerValue.contains(sqlInjectionChar)) {
                String errorMessage = String.format("参数[%s]中包含SQL注入风险字符: %s", paramName, value);
                throw new BizException(errorMessage);
            }
        }
    }
}
