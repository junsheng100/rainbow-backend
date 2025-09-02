# 🚀 Rainbow Backend 快速开始指南

## 📋 项目简介

Rainbow Backend 是一个基于 Spring Boot 2.7.12 构建的企业级后端系统，提供完整的用户管理、系统监控、任务调度、模板管理和 API 文档功能。

## ⚡ 5分钟快速启动

### 🐳 Docker 部署（推荐）

如果您想快速体验系统，推荐使用 Docker 部署：

```bash
# 克隆项目
git clone https://github.com/junsheng100/rainbow-backend.git
cd rainbow-backend

# 进入 Docker 目录
cd docker

# 一键部署（Linux/macOS）
./scripts/docker-deploy.sh

# 一键部署（Windows）
scripts\docker-deploy.bat
```

部署成功后访问：
- 🌐 **应用地址**: http://localhost:8080
- 📚 **API文档**: http://localhost:8080/swagger-ui.html

### 🔧 传统部署

#### 1. 环境检查

确保您的系统已安装：
- ✅ JDK 1.8+
- ✅ Maven 3.6+
- ✅ MySQL 8.0+
- ✅ Redis 6.0+

### 2. 一键启动

#### Linux/Mac 用户
```bash
./start.sh
```

#### Windows 用户
```cmd
start.bat
```

### 3. 访问应用

启动成功后，在浏览器中访问：
- 🌐 **应用首页**: http://localhost:8080
- 📚 **API文档**: http://localhost:8080/swagger-ui.html

## 🔧 手动启动步骤

如果您更喜欢手动操作，请按以下步骤进行：

### 1. 克隆项目
```bash
git clone https://github.com/junsheng100/rainbow-backend.git
cd rainbow-backend
```

### 2. 配置数据库
```sql
CREATE DATABASE db_rainbow CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 修改配置文件
编辑 `rainbow-server/rainbow-system/src/main/resources/application-dev.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/db_rainbow?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
  redis:
    host: localhost
    port: 6379
    password: your_redis_password
    database: 0
```

### 4. 编译项目
```bash
mvn clean install
```

### 5. 启动应用
```bash
mvn spring-boot:run -pl rainbow-server/rainbow-system
```

## 🎯 核心功能体验

### 用户管理
- 用户注册、登录
- 角色权限管理
- 用户信息管理

### 系统监控
- CPU、内存、磁盘监控
- JVM 性能监控
- 系统资源统计

### 任务调度
- 定时任务管理
- 任务执行监控
- 任务日志记录

### API文档
- 自动生成API文档
- 在线接口测试
- 接口版本管理

## 🔍 常见问题

### Q: 启动失败，提示端口被占用
**A**: 修改 `application.yml` 中的端口配置，或停止占用端口的进程

### Q: 数据库连接失败
**A**: 检查数据库服务是否启动，配置信息是否正确

### Q: Redis连接失败
**A**: 检查Redis服务是否启动，密码配置是否正确

### Q: 编译失败
**A**: 检查JDK和Maven版本，确保版本兼容

## 📚 下一步

- 📖 阅读 [开发指南](DEVELOPMENT_GUIDE.md)
- 🏗️ 了解 [项目架构](PROJECT_SUMMARY.md)
- 🔧 查看 [详细文档](../README_CN.md)

## 🆘 获取帮助

- 📧 邮箱: junsheng100@foxmail.com
- 🐛 问题反馈: [GitHub Issues](https://github.com/junsheng100/rainbow-backend/issues)
- 📖 项目地址: https://github.com/junsheng100/rainbow-backend

---

**🎉 恭喜！您已成功启动 Rainbow Backend 系统！**

现在可以开始探索系统的各项功能，或者开始您的开发之旅。
