# 🐳 Docker 部署目录

本目录包含 Rainbow Backend 项目的所有 Docker 相关文件和配置。

## 📁 目录结构

```
docker/
├── README.md                    # 本说明文件
├── Dockerfile                   # 应用镜像构建文件
├── Dockerfile.multi-stage       # 多阶段构建文件
├── docker-compose.yml           # Docker Compose 配置
├── scripts/                     # 部署脚本
│   ├── docker-deploy.sh        # Linux/macOS 部署脚本
│   └── docker-deploy.bat       # Windows 部署脚本
├── docs/                        # 文档
│   └── DOCKER_DEPLOY.md        # Docker 部署指南
├── config/                      # 应用配置目录
├── mysql/                       # MySQL 配置
│   ├── init/                    # 初始化脚本目录
│   └── conf/                    # MySQL 配置文件
│       └── my.cnf              # MySQL 主配置文件
├── redis/                       # Redis 配置
│   └── redis.conf              # Redis 配置文件
└── nginx/                       # Nginx 配置
    ├── nginx.conf              # Nginx 主配置文件
    ├── conf.d/                 # 站点配置目录
    │   └── rainbow-backend.conf # 应用站点配置
    └── ssl/                    # SSL 证书目录
```

## 🚀 快速开始

### 一键部署

#### Linux/macOS
```bash
cd docker/scripts
chmod +x docker-deploy.sh
./docker-deploy.sh
```

#### Windows
```cmd
cd docker\scripts
docker-deploy.bat
```

### 手动部署
```bash
cd docker
docker-compose up -d
```

## 🔧 配置说明

### 环境变量
主要的环境变量配置在 `docker-compose.yml` 文件中，包括：
- 数据库连接配置
- Redis 连接配置
- JWT 密钥配置
- 应用端口配置

### 端口映射
- 应用服务: 8080
- MySQL: 3306
- Redis: 6379
- Nginx HTTP: 80
- Nginx HTTPS: 443

### 数据卷
- `mysql_data`: MySQL 数据持久化
- `redis_data`: Redis 数据持久化
- `./logs`: 应用日志目录
- `./upload`: 文件上传目录
- `./docker/config`: 应用配置目录

## 📚 文档

- [Docker 部署指南](docs/DOCKER_DEPLOY.md) - 详细的部署说明和配置指南

## 🆘 获取帮助

- 📧 邮箱: junsheng100@foxmail.com
- 🐛 问题反馈: [GitHub Issues](https://github.com/junsheng100/rainbow-backend/issues)
- 📖 项目地址: https://github.com/junsheng100/rainbow-backend
