# XSS 防护功能使用说明

## 功能介绍

XSS（Cross-Site Scripting，跨站脚本攻击）是一种常见的 Web 应用安全漏洞，攻击者通过在网页中注入恶意脚本，当用户浏览网页时，恶意脚本会被执行，从而达到攻击目的。

本项目实现了一套完整的 XSS 防护机制，包括：

1. 请求参数过滤
2. 请求头检查
3. 响应内容处理

## 实现原理

1. **XssHttpServletRequestWrapper**：包装 HttpServletRequest，重写获取参数的方法，对参数进行 XSS 过滤
2. **XssFilter**：过滤器，拦截请求并使用 XssHttpServletRequestWrapper 包装类处理请求参数
3. **XssInterceptor**：拦截器，在控制器方法执行前进行 XSS 检查

## 防护策略

1. **HTML 转义**：将特殊字符转换为 HTML 实体，如 `<` 转换为 `&lt;`，`>` 转换为 `&gt;` 等
2. **特征检测**：检测请求中是否包含 XSS 攻击特征，如 `<script>`、`javascript:` 等
3. **路径排除**：对于特定的路径，如静态资源、Swagger 文档等，不进行 XSS 过滤

## 配置说明

### 排除路径

在 `XssFilter` 类中，我们配置了以下排除路径：

```java
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
```

如果需要添加更多排除路径，可以在 `XssFilter` 类的 `initExcludes` 方法中添加。

### 自定义 XSS 检测规则

在 `XssInterceptor` 类中，我们配置了以下 XSS 攻击特征正则表达式：

```java
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
```

如果需要添加更多 XSS 攻击特征，可以在 `XssInterceptor` 类中添加新的正则表达式。

## 使用方法

XSS 防护功能已经全局启用，无需额外配置。所有请求都会经过 XSS 过滤器和拦截器的处理。

如果需要对特定接口禁用 XSS 防护，可以在 `WebMvcConfig` 类的 `addInterceptors` 方法中添加排除路径。

## 注意事项

1. 对于富文本编辑器等需要提交 HTML 内容的场景，建议使用专门的接口，并在 `XssFilter` 和 `WebMvcConfig` 中将这些接口添加到排除路径中。

2. XSS 防护可能会影响正常的 HTML 内容提交，如果发现某些功能受到影响，请检查是否需要将相关接口添加到排除路径中。

3. 本 XSS 防护机制主要针对反射型 XSS 攻击，对于存储型 XSS 攻击，还需要在数据存储和展示环节进行额外处理。
