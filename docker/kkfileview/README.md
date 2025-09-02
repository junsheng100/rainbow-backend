# kkFileView 文件预览服务

## 📄 服务说明

本目录包含 [kkFileView](https://github.com/kekingcn/kkFileView) 文件预览服务的 Docker 配置和部署文件。

## 🎯 功能特性

- **多格式支持**: Office、PDF、图片、文本、压缩包等多种文件格式
- **在线预览**: 无需下载，直接在浏览器中预览文件内容
- **格式转换**: 支持文件格式转换和在线查看
- **高性能**: 基于 LibreOffice 的高效文档转换

## 📁 目录结构

```
kkfileview/
├── README.md                    # 本说明文件
├── config/                      # 配置文件目录
├── logs/                        # 日志文件目录
└── cache/                       # 缓存文件目录
```

## 🚀 快速启动

### 1. **使用 Docker Compose**

```bash
# 启动 kkFileView 服务
docker-compose up -d kkfileview

# 查看服务状态
docker-compose ps kkfileview

# 查看服务日志
docker-compose logs -f kkfileview
```

### 2. **独立启动**

```bash
# 启动容器
docker run -d \
  --name rainbow-kkfileview \
  -p 8012:8012 \
  -v $(pwd)/config:/opt/kkFileView/config \
  -v $(pwd)/logs:/opt/kkFileView/logs \
  -v $(pwd)/cache:/opt/kkFileView/cache \
  -e KK_CONTEXT_PATH=/ \
  -e KK_OFFICE_PREVIEW_TYPE=libreoffice \
  -e KK_OFFICE_PREVIEW_MAX_TASKS=20 \
  -e KK_OFFICE_PREVIEW_TASK_QUEUE_TIMEOUT=3000 \
  keking/kkfileview:latest
```

## ⚙️ 配置说明

### 环境变量

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `KK_CONTEXT_PATH` | `/` | 应用上下文路径 |
| `KK_OFFICE_PREVIEW_TYPE` | `libreoffice` | Office 预览类型 |
| `KK_OFFICE_PREVIEW_MAX_TASKS` | `20` | 最大并发任务数 |
| `KK_OFFICE_PREVIEW_TASK_QUEUE_TIMEOUT` | `3000` | 任务队列超时时间(ms) |

### 端口配置

- **服务端口**: 8012
- **访问地址**: http://localhost:8012

## 🔧 集成配置

### 1. **Rainbow Backend 配置**

在 `application.yml` 中添加：

```yaml
file:
  preview:
    enabled: true
    kkfileview:
      url: http://localhost:8012
      timeout: 30000
      cache-enabled: true
      cache-expire: 3600
```

### 2. **Nginx 反向代理**

在 Nginx 配置中添加：

```nginx
location /file-preview/ {
    proxy_pass http://kkfileview:8012/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
}
```

## 📊 监控和日志

### 1. **健康检查**

```bash
# 检查服务状态
curl http://localhost:8012/actuator/health

# 检查预览功能
curl "http://localhost:8012/onlinePreview?url=test"
```

### 2. **日志查看**

```bash
# 查看实时日志
docker-compose logs -f kkfileview

# 查看容器内日志
docker exec -it rainbow-kkfileview tail -f /opt/kkFileView/logs/kkFileView.log
```

## 🐛 常见问题

### 1. **服务启动失败**
- 检查端口 8012 是否被占用
- 确认 Docker 镜像是否正确拉取
- 查看容器日志获取详细错误信息

### 2. **文件预览失败**
- 检查文件格式是否支持
- 确认 LibreOffice 组件是否正常
- 查看服务日志获取错误详情

### 3. **性能问题**
- 调整并发任务数量
- 优化缓存配置
- 增加服务器资源

## 📚 相关资源

- **[kkFileView 官方仓库](https://github.com/kekingcn/kkFileView)**
- **[kkFileView 官方文档](https://kkview.cn)**
- **[Docker 镜像](https://hub.docker.com/r/keking/kkfileview)**
- **[在线演示](https://demo.kkview.cn)**

## 🤝 技术支持

如果您在使用过程中遇到问题：

1. 查看 [kkFileView 官方文档](https://kkview.cn)
2. 提交 Issue 到 [kkFileView 仓库](https://github.com/kekingcn/kkFileView/issues)
3. 查看 [Rainbow Backend 文档](../README.md)

---

*感谢 [kkFileView](https://github.com/kekingcn/kkFileView) 团队提供的优秀开源文件预览解决方案！*
