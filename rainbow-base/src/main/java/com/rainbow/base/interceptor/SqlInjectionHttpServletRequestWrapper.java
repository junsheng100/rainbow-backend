package com.rainbow.base.interceptor;

import com.rainbow.base.exception.BizException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * SQL注入检测包装类
 */
public class SqlInjectionHttpServletRequestWrapper extends HttpServletRequestWrapper {

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
     * 需要检测的参数类型
     */
    private static final String[] PARAMETER_TYPES = {"string", "number", "boolean"};

    public SqlInjectionHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return checkSqlInjection(name, value);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        int length = values.length;
        String[] newValues = new String[length];
        for (int i = 0; i < length; i++) {
            newValues[i] = checkSqlInjection(name, values[i]);
        }
        return newValues;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = super.getParameterMap();
        Map<String, String[]> newMap = new LinkedHashMap<>();
        if (map != null) {
            for (Map.Entry<String, String[]> entry : map.entrySet()) {
                String[] values = entry.getValue();
                if (values != null) {
                    String[] newValues = new String[values.length];
                    for (int i = 0; i < values.length; i++) {
                        newValues[i] = checkSqlInjection(entry.getKey(), values[i]);
                    }
                    newMap.put(entry.getKey(), newValues);
                }
            }
        }
        return newMap;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return checkSqlInjection(name, value);
    }

    /**
     * 检查是否存在SQL注入
     */
    private String checkSqlInjection(String paramName, String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }

        // 检查是否包含SQL注入特征
        for (Pattern pattern : SQL_INJECTION_PATTERNS) {
            if (pattern.matcher(value).matches()) {
                String errorMessage = String.format("参数[%s]中包含非法字符，可能存在SQL注入风险: %s", paramName, value);
                throw new BizException(errorMessage);
            }
        }

        // 检查是否包含特殊字符
        if (containsSqlInjectionChars(value)) {
            String errorMessage = String.format("参数[%s]中包含SQL注入风险字符: %s", paramName, value);
            throw new BizException(errorMessage);
        }

        return value;
    }

    /**
     * 检查是否包含SQL注入风险字符
     */
    private boolean containsSqlInjectionChars(String value) {
        // SQL注入风险字符
        String[] sqlInjectionChars = {
                "';", "--;", "/*", "*/", "@@", "@",
                "char ", "nchar ",
                "varchar ", "nvarchar ",
                "alter ", "begin ", "cast ", "create ", "cursor ", "declare ", "delete ", "drop ",
                "end ", "exec ", "execute ", "fetch ", "insert ", "kill ", "select ", "sys ",
                "sysobjects ", "syscolumns ", "table ", "update "
        };

        value = value.toLowerCase();
        for (String sqlInjectionChar : sqlInjectionChars) {
            if (value.contains(sqlInjectionChar)) {
                return true;
            }
        }

        return false;
    }
}
