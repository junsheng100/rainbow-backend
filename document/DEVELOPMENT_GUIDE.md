# Rainbow Backend 开发指南

## 🎯 开发环境准备

### 必需环境

- **JDK**: 1.8 或更高版本
- **Maven**: 3.6 或更高版本
- **IDE**: 推荐使用 IntelliJ IDEA 或 Cursor IDE
- **数据库**: MySQL 8.0 或更高版本
- **缓存**: Redis 6.0 或更高版本

### 推荐环境

- **JDK**: OpenJDK 8 或 Oracle JDK 8
- **Maven**: Apache Maven 3.8+
- **IDE**: IntelliJ IDEA 2023.1+ 或 Cursor IDE
- **数据库**: MySQL 8.0.33+
- **缓存**: Redis 6.2+

## 🚀 项目启动

### 1. 克隆项目

```bash
git clone https://github.com/junsheng100/rainbow-backend.git
cd rainbow-backend
```

### 2. 导入项目

在 IDE 中导入项目，选择 Maven 项目类型。

### 3. 配置数据库

创建数据库：
```sql
CREATE DATABASE db_rainbow CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改配置文件 `rainbow-server/rainbow-system/src/main/resources/application-dev.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/db_rainbow?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 4. 配置Redis

修改配置文件：
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: your_redis_password
    database: 0
```

### 5. 启动应用

```bash
# 编译项目
mvn clean install

# 启动应用
mvn spring-boot:run -pl rainbow-server/rainbow-system
```

## 📚 开发规范

### 代码风格

#### 1. 命名规范

```java
// 类名：大驼峰命名
public class UserInfoController extends BaseController<UserInfo, String, UserInfoService> {
  
    // 方法名：小驼峰命名
    public Result<UserInfo> getUserInfo(String userId) {
        // 变量名：小驼峰命名
        UserInfo userInfo = userService.get(userId);
        return Result.success(userInfo);
    }
  
    // 常量：全大写+下划线
    private static final String USER_CACHE_KEY = "user:info:";
    
    // 包名：全小写
    package com.rainbow.user.controller;
}
```

#### 2. 注释规范

```java
/**
 * 用户信息控制器
 * 
 * @author your-name
 * @since 1.0.0
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理")
public class UserInfoController extends BaseController<UserInfo, String, UserInfoService> {
  
    /**
     * 获取用户信息
     * 
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/{userId}")
    @OperLog("获取用户信息")
    public Result<UserInfo> getUserInfo(@PathVariable String userId) {
        return Result.success(service.get(userId));
    }
}
```

#### 3. 异常处理

```java
try {
    // 业务逻辑
    UserInfo userInfo = userService.get(userId);
    return Result.success(userInfo);
} catch (BizException e) {
    log.error("获取用户信息失败，用户ID: {}", userId, e);
    return Result.error(e.getCode(), e.getMessage());
} catch (Exception e) {
    log.error("系统异常，用户ID: {}", userId, e);
    return Result.error(HttpCode.INTERNAL_SERVER_ERROR, "系统异常");
}
```

### 架构规范

#### 1. 分层架构

```
Controller → Service → Dao → Repository
    ↓           ↓       ↓        ↓
  请求处理   业务逻辑   数据访问   数据持久化
```

#### 2. 依赖注入

```java
@Service
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo, String, UserInfoDao> {
  
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private UserInfoRepository userInfoRepository;
}
```

#### 3. 事务管理

```java
@Service
@Transactional(rollbackFor = Exception.class)
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo, String, UserInfoDao> {
  
    @Transactional(readOnly = true)
    public UserInfo getUserInfo(String userId) {
        return get(userId);
    }
    
    public UserInfo createUser(UserInfo userInfo) {
        // 自动开启事务
        return store(userInfo);
    }
}
```

## 🔐 安全开发

### 1. 认证授权

```java
// 使用 @PreAuthorize 进行权限控制
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin/users")
public Result<List<UserInfo>> getAllUsers() {
    return Result.success(userService.findAll());
}

// 使用 @NoRepeatSubmit 防止重复提交
@NoRepeatSubmit(interval = 5, timeUnit = TimeUnit.SECONDS)
@PostMapping("/create")
public Result<UserInfo> createUser(@RequestBody @Valid UserInfo userInfo) {
    return Result.success(userService.store(userInfo));
}
```

### 2. 数据验证

```java
// 使用 @Valid 进行参数验证
@PostMapping("/update")
public Result<UserInfo> updateUser(@RequestBody @Valid UserInfo userInfo) {
    return Result.success(userService.store(userInfo));
}

// 实体类验证注解
@Entity
@Table(name = "user_info")
public class UserInfo extends BaseEntity {
  
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20之间")
    @Column(unique = true)
    private String userName;
  
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能少于6位")
    private String password;
}
```

### 3. SQL注入防护

```java
// 使用参数化查询
@Query("SELECT u FROM UserInfo u WHERE u.userName = :userName")
UserInfo findByUserName(@Param("userName") String userName);

// 使用 JPA 方法命名查询
UserInfo findByUserNameAndStatus(String userName, String status);
```

## ⚡ 性能优化

### 1. 数据库优化

```java
// 使用分页查询避免大量数据查询
@PostMapping("/page")
public Result<PageData<UserInfo>> getUserPage(@RequestBody BaseVo<UserInfo> vo) {
    return Result.success(service.page(vo));
}

// 使用批量操作提高性能
@PostMapping("/batch/delete")
public Result<Boolean> deleteUsers(@RequestBody CommonVo<List<String>> vo) {
    return Result.success(service.deleteInBatch(vo.getData()));
}

// 使用 @EntityGraph 优化关联查询
@EntityGraph(attributePaths = {"roles", "permissions"})
UserInfo findWithRolesAndPermissionsById(String id);
```

### 2. 缓存策略

```java
// 使用 Redis 缓存热点数据
@Service
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo, String, UserInfoDao> {
  
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
  
    @Override
    public UserInfo get(String id) {
        // 先从缓存获取
        String cacheKey = "user:info:" + id;
        UserInfo userInfo = (UserInfo) redisTemplate.opsForValue().get(cacheKey);
      
        if (userInfo == null) {
            // 缓存未命中，从数据库获取
            userInfo = super.get(id);
            if (userInfo != null) {
                // 设置缓存，过期时间30分钟
                redisTemplate.opsForValue().set(cacheKey, userInfo, 30, TimeUnit.MINUTES);
            }
        }
      
        return userInfo;
    }
}
```

### 3. 异步处理

```java
// 使用 @Async 进行异步处理
@Service
public class EmailService {
  
    @Async
    public void sendEmail(String to, String subject, String content) {
        // 异步发送邮件
        log.info("发送邮件到: {}", to);
    }
}

// 配置异步线程池
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
  
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.initialize();
        return executor;
    }
}
```

## 🧪 测试指南

### 1. 单元测试

```java
@ExtendWith(MockitoExtension.class)
class UserInfoServiceTest {
  
    @Mock
    private UserInfoRepository userInfoRepository;
    
    @InjectMocks
    private UserInfoServiceImpl userInfoService;
    
    @Test
    void testGetUserInfo() {
        // Given
        String userId = "test-user-id";
        UserInfo expectedUser = new UserInfo();
        expectedUser.setId(userId);
        expectedUser.setUserName("testuser");
        
        when(userInfoRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        
        // When
        UserInfo result = userInfoService.get(userId);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUserName()).isEqualTo("testuser");
        verify(userInfoRepository).findById(userId);
    }
}
```

### 2. 集成测试

```java
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class UserInfoControllerIntegrationTest {
  
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testGetUserInfo() {
        // Given
        String userId = "test-user-id";
        
        // When
        ResponseEntity<UserInfo> response = restTemplate.getForEntity(
            "/api/user/{userId}", UserInfo.class, userId);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}
```

## 📝 日志规范

### 1. 日志级别

```java
// ERROR: 系统错误和异常
log.error("获取用户信息失败，用户ID: {}", userId, e);

// WARN: 警告信息
log.warn("用户登录失败次数过多，用户ID: {}", userId);

// INFO: 一般信息
log.info("用户登录成功，用户ID: {}", userId);

// DEBUG: 调试信息
log.debug("查询用户信息，参数: userId={}", userId);
```

### 2. 日志格式

```java
// 使用占位符，避免字符串拼接
log.info("用户 {} 在 {} 登录系统", userName, new Date());

// 记录关键业务操作
@OperLog("创建用户")
public Result<UserInfo> createUser(@RequestBody @Valid UserInfo userInfo) {
    log.info("开始创建用户，用户名: {}", userInfo.getUserName());
    // 业务逻辑
    log.info("用户创建成功，用户ID: {}", result.getId());
    return result;
}
```

## 🔧 工具使用

### 1. 基础工具类

```java
// 字符串工具
if (StringUtils.isBlank(userName)) {
    throw new BizException("用户名不能为空");
}

// 日期工具
String formattedDate = DateTools.format(new Date(), "yyyy-MM-dd HH:mm:ss");

// JWT工具
String token = JwtTokenUtil.generateToken(userInfo);
```

### 2. 自定义注解

```java
// 防重复提交
@NoRepeatSubmit(interval = 5, timeUnit = TimeUnit.SECONDS)
@PostMapping("/create")
public Result<UserInfo> createUser(@RequestBody UserInfo userInfo) {
    return Result.success(userService.store(userInfo));
}

// 操作日志
@OperLog("更新用户信息")
@PutMapping("/update")
public Result<UserInfo> updateUser(@RequestBody UserInfo userInfo) {
    return Result.success(userService.store(userInfo));
}
```

## 🚀 部署指南

### 🐳 Docker 部署（推荐）

```bash
# 进入 Docker 目录
cd docker

# 一键部署
./scripts/docker-deploy.sh

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

详细说明请参考：[Docker 部署指南](../docker/docs/DOCKER_DEPLOY.md)

更多文档请查看：[📚 文档中心](../README.md)

### 🔧 传统部署

#### 1. **开发环境**

```bash
# 启动应用
mvn spring-boot:run -pl rainbow-server/rainbow-system

# 指定配置文件
mvn spring-boot:run -pl rainbow-server/rainbow-system -Dspring.profiles.active=dev
```

#### 2. **生产环境**

```bash
# 打包
mvn clean package -Dmaven.test.skip=true

# 启动
java -jar -Dspring.profiles.active=prod target/rainbow-system.jar

# 指定JVM参数
java -Xms2g -Xmx4g -XX:+UseG1GC \
     -Dspring.profiles.active=prod \
     -jar target/rainbow-system.jar
```

#### 3. **Docker 镜像构建**

```dockerfile
FROM openjdk:8-jre-alpine
VOLUME /tmp
COPY target/rainbow-system.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 📚 常见问题

### 1. 启动问题

**问题**: 应用启动失败，提示端口被占用
**解决**: 修改 `application.yml` 中的端口配置，或停止占用端口的进程

**问题**: 数据库连接失败
**解决**: 检查数据库配置、网络连接和数据库服务状态

### 2. 性能问题

**问题**: 接口响应慢
**解决**: 检查数据库查询、缓存配置和JVM参数

**问题**: 内存使用过高
**解决**: 调整JVM堆内存参数，检查内存泄漏

### 3. 安全问题

**问题**: JWT token过期
**解决**: 检查token有效期配置，实现token刷新机制

**问题**: 权限验证失败
**解决**: 检查用户角色和权限配置

## 🔮 最佳实践

### 1. 代码组织

- 按功能模块组织代码
- 保持类的单一职责
- 使用接口和抽象类
- 避免循环依赖

### 2. 异常处理

- 统一异常处理机制
- 记录详细的错误日志
- 返回友好的错误信息
- 避免暴露系统内部信息

### 3. 性能优化

- 合理使用缓存
- 优化数据库查询
- 使用连接池
- 异步处理耗时操作

### 4. 安全防护

- 输入验证和过滤
- 权限控制
- 日志审计
- 定期安全更新

---

*本开发指南为 Rainbow Backend 项目的开发规范，请严格遵循以确保代码质量和系统稳定性。*
