# 🐳 Rainbow Backend Docker 部署指南

## 📋 概述

本文档介绍如何使用 Docker 和 Docker Compose 部署 Rainbow Backend 系统。Docker 部署提供了以下优势：

- 🚀 **快速部署**: 一键启动所有服务
- 🔧 **环境一致**: 确保开发、测试、生产环境一致
- 📦 **易于管理**: 统一的服务管理和监控
- 🔒 **安全隔离**: 服务间网络隔离和资源限制

## 🎯 部署架构

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Nginx (80/443)│    │ Rainbow Backend │    │   MySQL (3306)  │
│   (反向代理)     │◄──►│   (8080)        │◄──►│   (数据库)      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │   Redis (6379)  │
                       │   (缓存)        │
                       └─────────────────┘
```

## 🚀 快速部署

### 1. 环境要求

- **Docker**: 20.10+
- **Docker Compose**: 2.0+
- **操作系统**: Linux/macOS/Windows
- **内存**: 至少 4GB RAM
- **磁盘**: 至少 10GB 可用空间

### 2. 一键部署

#### Linux/macOS 用户
```bash
# 克隆项目
git clone https://github.com/junsheng100/rainbow-backend.git
cd rainbow-backend

# 一键部署
./docker-deploy.sh

# 或者包含 Nginx 反向代理
./docker-deploy.sh --with-nginx
```

#### Windows 用户
```cmd
# 克隆项目
git clone https://github.com/junsheng100/rainbow-backend.git
cd rainbow-backend

# 一键部署
docker-deploy.bat
```

### 3. 访问应用

部署成功后，在浏览器中访问：
- 🌐 **应用首页**: http://localhost:8080
- 📚 **API文档**: http://localhost:8080/swagger-ui.html
- 🔍 **健康检查**: http://localhost:8080/actuator/health

如果启用了 Nginx：
- 🌐 **HTTP**: http://localhost (自动重定向到 HTTPS)
- 🔒 **HTTPS**: https://localhost

## 🔧 手动部署

### 1. 准备环境

```bash
# 创建必要的目录
mkdir -p logs upload docker/config
mkdir -p docker/mysql/{init,conf}
mkdir -p docker/redis
mkdir -p docker/nginx/{conf.d,ssl}
```

### 2. 生成 SSL 证书

```bash
# 生成自签名证书
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
    -keyout docker/nginx/ssl/rainbow-backend.key \
    -out docker/nginx/ssl/rainbow-backend.crt \
    -subj "/C=CN/ST=Beijing/L=Beijing/O=Rainbow/OU=IT/CN=localhost"
```

### 3. 构建和启动

```bash
# 构建镜像
docker-compose build

# 启动服务
docker-compose up -d

# 查看状态
docker-compose ps
```

## 📁 文件结构

```
rainbow-backend/
├── Dockerfile                    # 应用镜像构建文件
├── Dockerfile.multi-stage        # 多阶段构建文件
├── docker-compose.yml            # Docker Compose 配置
├── docker-deploy.sh              # Linux/macOS 部署脚本
├── docker-deploy.bat             # Windows 部署脚本
├── docker/                       # Docker 配置目录
│   ├── config/                   # 应用配置
│   ├── mysql/                    # MySQL 配置
│   │   ├── init/                 # 初始化脚本
│   │   └── conf/                 # MySQL 配置
│   ├── redis/                    # Redis 配置
│   │   └── redis.conf            # Redis 配置文件
│   └── nginx/                    # Nginx 配置
│       ├── nginx.conf            # Nginx 主配置
│       ├── conf.d/               # 站点配置
│       │   └── rainbow-backend.conf
│       └── ssl/                  # SSL 证书
├── logs/                         # 应用日志目录
└── upload/                       # 文件上传目录
```

## ⚙️ 配置说明

### 1. 环境变量

```yaml
# docker-compose.yml 中的环境变量
environment:
  - SPRING_PROFILES_ACTIVE=prod                    # 激活生产环境配置
  - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/db_rainbow  # 数据库连接
  - SPRING_DATASOURCE_USERNAME=rainbow             # 数据库用户名
  - SPRING_DATASOURCE_PASSWORD=rainbow123          # 数据库密码
  - SPRING_REDIS_HOST=redis                       # Redis 主机
  - SPRING_REDIS_PORT=6379                        # Redis 端口
  - SPRING_REDIS_PASSWORD=redis123                # Redis 密码
  - JWT_SECRET=your_jwt_secret_key_here           # JWT 密钥
```

### 2. 端口映射

```yaml
ports:
  - "8080:8080"    # 应用服务端口
  - "3306:3306"    # MySQL 端口
  - "6379:6379"    # Redis 端口
  - "80:80"        # Nginx HTTP 端口
  - "443:443"      # Nginx HTTPS 端口
```

### 3. 数据卷

```yaml
volumes:
  - mysql_data:/var/lib/mysql          # MySQL 数据持久化
  - redis_data:/data                   # Redis 数据持久化
  - ./logs:/app/logs                   # 应用日志
  - ./upload:/app/upload               # 文件上传
  - ./docker/config:/app/config        # 应用配置
```

## 🔍 服务管理

### 1. 查看服务状态

```bash
# 查看所有服务状态
docker-compose ps

# 查看服务日志
docker-compose logs -f

# 查看特定服务日志
docker-compose logs -f rainbow-backend
```

### 2. 服务操作

```bash
# 启动服务
docker-compose up -d

# 停止服务
docker-compose down

# 重启服务
docker-compose restart

# 重启特定服务
docker-compose restart rainbow-backend
```

### 3. 健康检查

```bash
# 检查应用健康状态
curl http://localhost:8080/actuator/health

# 检查数据库连接
docker-compose exec mysql mysqladmin ping -h localhost -u root -proot123

# 检查 Redis 连接
docker-compose exec redis redis-cli -a redis123 ping
```

## 🔒 安全配置

### 1. 密码安全

```bash
# 修改默认密码
# 编辑 docker-compose.yml 文件，修改以下密码：
# - MYSQL_ROOT_PASSWORD
# - MYSQL_PASSWORD
# - SPRING_REDIS_PASSWORD
# - JWT_SECRET
```

### 2. 网络隔离

```yaml
# 使用自定义网络
networks:
  rainbow-network:
    driver: bridge
    ipam:
      config:
        - subnet: 172.20.0.0/16
```

### 3. SSL 配置

```bash
# 使用正式 SSL 证书
# 将证书文件放置在 docker/nginx/ssl/ 目录下：
# - rainbow-backend.crt (证书文件)
# - rainbow-backend.key (私钥文件)
```

## 📊 性能优化

### 1. JVM 参数优化

```dockerfile
# Dockerfile 中的 JVM 参数
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

### 2. 数据库优化

```ini
# docker/mysql/conf/my.cnf
innodb_buffer_pool_size = 256M
innodb_log_file_size = 64M
query_cache_size = 32M
```

### 3. Redis 优化

```conf
# docker/redis/redis.conf
maxmemory 256mb
maxmemory-policy allkeys-lru
```

## 🚨 故障排除

### 1. 常见问题

#### 端口被占用
```bash
# 检查端口占用
lsof -i :8080
netstat -tulpn | grep :8080

# 修改端口映射
# 编辑 docker-compose.yml 中的 ports 配置
```

#### 数据库连接失败
```bash
# 检查 MySQL 服务状态
docker-compose logs mysql

# 检查网络连接
docker-compose exec rainbow-backend ping mysql
```

#### 内存不足
```bash
# 检查系统内存
free -h

# 调整 JVM 参数
# 修改 Dockerfile 中的 JAVA_OPTS
```

### 2. 日志分析

```bash
# 查看应用日志
docker-compose logs -f rainbow-backend

# 查看数据库日志
docker-compose logs -f mysql

# 查看 Redis 日志
docker-compose logs -f redis
```

### 3. 性能监控

```bash
# 查看容器资源使用
docker stats

# 查看系统资源
htop
iostat
```

## 🔄 更新部署

### 1. 代码更新

```bash
# 拉取最新代码
git pull origin main

# 重新构建镜像
docker-compose build --no-cache

# 重启服务
docker-compose up -d
```

### 2. 配置更新

```bash
# 修改配置文件后重启服务
docker-compose restart rainbow-backend

# 或者重新构建镜像
docker-compose build rainbow-backend
docker-compose up -d rainbow-backend
```

### 3. 数据库迁移

```bash
# 备份数据库
docker-compose exec mysql mysqldump -u root -proot123 db_rainbow > backup.sql

# 恢复数据库
docker-compose exec -T mysql mysql -u root -proot123 db_rainbow < backup.sql
```

## 📚 最佳实践

### 1. 生产环境

- 使用正式 SSL 证书
- 修改默认密码
- 配置防火墙规则
- 设置日志轮转
- 配置监控告警

### 2. 备份策略

- 定期备份数据库
- 备份配置文件
- 备份上传文件
- 测试恢复流程

### 3. 监控告警

- 配置健康检查
- 设置资源监控
- 配置日志监控
- 设置告警通知

## 🆘 获取帮助

- 📧 邮箱: junsheng100@foxmail.com
- 🐛 问题反馈: [GitHub Issues](https://github.com/junsheng100/rainbow-backend/issues)
- 📖 项目地址: https://github.com/junsheng100/rainbow-backend

---

*本文档为 Rainbow Backend 项目的 Docker 部署指南，帮助用户快速部署和管理系统。*
