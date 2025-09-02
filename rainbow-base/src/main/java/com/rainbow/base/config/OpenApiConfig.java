package com.rainbow.base.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * OpenAPI 配置类
 * 配置 Swagger UI 的 JWT 认证和 API 文档信息
 * 
 * @author rainbow
 * @since 1.0.0
 */
@Configuration
public class OpenApiConfig {

  private static final String JWT_SCHEME_NAME = "JWT";
  private static final String JWT_DESCRIPTION = "JWT 认证令牌。请在登录后获取 token，然后在下方输入框中输入：Bearer {token}";

  @Value("${server.port:80}")
  private Integer port ;
  /**
   * 配置 OpenAPI 文档
   */
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .info(createApiInfo())
            .servers(createServers())
            .addSecurityItem(new SecurityRequirement().addList(JWT_SCHEME_NAME))
            .components(new Components()
                    .addSecuritySchemes(JWT_SCHEME_NAME, createJWTSecurityScheme()));
  }

  /**
   * 创建 API 信息
   */
  private Info createApiInfo() {
    return new Info()
            .title("Rainbow API")
            .version("1.0.0")
            .description("Rainbow 认证授权系统 API 文档\n\n"
//                   + "## 使用说明\n\n"
//                   + "1. 首先调用登录接口获取 JWT token\n"
//                   + "2. 点击右上角的 **Authorize** 按钮\n"
//                   + "3. 在弹出框中输入：`Bearer {your_token}`\n"
//                   + "4. 点击 **Authorize** 确认\n"
//                   + "5. 现在可以调用需要认证的 API 了\n\n"
//                   + "## 认证流程\n\n"
//                   + "1. **POST** `/auth/login` - 用户登录\n"
//                   + "2. 从响应中获取 `token` 字段\n"
//                   + "3. 在 Authorize 中输入：`Bearer {token}`\n"
//                   + "4. 调用其他需要认证的 API"
            )
            .contact(new Contact()
                    .name("Rainbow Team")
                    .email("support@rainbow.com")
                    .url("https://github.com/rainbow"))
            .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT"));
  }

  /**
   * 创建服务器配置
   */
  @SneakyThrows
  private List<Server> createServers() {
    List list = new ArrayList();

    InetAddress address = InetAddress.getLocalHost();
    String host = address.getHostAddress();
    String url = "http://"+host+":" + port;
    list.add(new Server()
            .url(url)
            .description("开发环境"));
//    list.add(new Server()
//            .url("https://api.rainbow.com")
//            .description("生产环境"));

    return list;
  }

  /**
   * 创建 JWT 安全方案
   */
  private SecurityScheme createJWTSecurityScheme() {
    return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name("Authorization")
            .description(JWT_DESCRIPTION);
  }
}