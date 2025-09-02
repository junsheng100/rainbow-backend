@echo off
chcp 65001 >nul

echo ==========================================
echo     Rainbow Backend Docker 部署脚本
echo ==========================================

REM 检查 Docker 环境
echo 🔍 检查 Docker 环境...
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 错误: 未找到 Docker，请先安装 Docker
    pause
    exit /b 1
)

docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 错误: 未找到 Docker Compose，请先安装 Docker Compose
    pause
    exit /b 1
)

echo ✅ Docker 环境检查通过

REM 创建必要的目录
echo 📁 创建必要的目录...
if not exist "logs" mkdir logs
if not exist "upload" mkdir upload
if not exist "docker\config" mkdir docker\config
if not exist "docker\mysql\init" mkdir docker\mysql\init
if not exist "docker\mysql\conf" mkdir docker\mysql\conf
if not exist "docker\redis" mkdir docker\redis
if not exist "docker\nginx\conf.d" mkdir docker\nginx\conf.d
if not exist "docker\nginx\ssl" mkdir docker\nginx\ssl

echo ✅ 目录创建完成

REM 生成自签名 SSL 证书
echo 🔐 生成自签名 SSL 证书...
if not exist "docker\nginx\ssl\rainbow-backend.crt" (
    openssl req -x509 -nodes -days 365 -newkey rsa:2048 ^
        -keyout docker\nginx\ssl\rainbow-backend.key ^
        -out docker\nginx\ssl\rainbow-backend.crt ^
        -subj "/C=CN/ST=Beijing/L=Beijing/O=Rainbow/OU=IT/CN=localhost"
    echo ✅ SSL 证书生成完成
) else (
    echo ⚠️  SSL 证书已存在，跳过生成
)

REM 构建镜像
echo 🔨 构建 Docker 镜像...
docker-compose down 2>nul
docker-compose build --no-cache
if %errorlevel% neq 0 (
    echo ❌ 镜像构建失败
    pause
    exit /b 1
)
echo ✅ 镜像构建完成

REM 启动服务
echo 🚀 启动服务...
docker-compose up -d mysql redis
if %errorlevel% neq 0 (
    echo ❌ 数据库服务启动失败
    pause
    exit /b 1
)

echo ⏳ 等待数据库服务启动...
timeout /t 30 /nobreak >nul

echo 🚀 启动 kkFileView 文件预览服务...
docker-compose up -d kkfileview
if %errorlevel% neq 0 (
    echo ❌ 文件预览服务启动失败
    pause
    exit /b 1
)

echo ⏳ 等待文件预览服务启动...
timeout /t 15 /nobreak >nul

docker-compose up -d rainbow-backend
if %errorlevel% neq 0 (
    echo ❌ 应用服务启动失败
    pause
    exit /b 1
)

echo ⏳ 等待应用服务启动...
timeout /t 20 /nobreak >nul

echo ✅ 服务启动完成

REM 检查服务状态
echo 🔍 检查服务状态...
echo.
docker-compose ps

echo.
echo 📊 服务健康状态:

REM 检查应用健康状态
curl -f http://localhost:8080/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ 应用服务运行正常
) else (
    echo ❌ 应用服务异常
)

REM 检查数据库连接
docker-compose exec -T mysql mysqladmin ping -h localhost -u root -proot123 >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ MySQL 服务运行正常
) else (
    echo ❌ MySQL 服务异常
)

REM 检查 Redis 连接
docker-compose exec -T redis redis-cli -a redis123 ping >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Redis 服务运行正常
) else (
    echo ❌ Redis 服务异常
)

REM 检查 kkFileView 服务
curl -f http://localhost:8012/onlinePreview >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ kkFileView 文件预览服务运行正常
) else (
    echo ❌ kkFileView 文件预览服务异常
)

REM 显示访问信息
echo.
echo 🎉 部署完成！
echo.
echo 📱 访问信息:
echo   应用地址: http://localhost:8080
echo   API文档: http://localhost:8080/swagger-ui.html
echo   健康检查: http://localhost:8080/actuator/health
echo   文件预览: http://localhost:8012
echo.
echo 🔧 管理命令:
echo   查看日志: docker-compose logs -f
echo   停止服务: docker-compose down
echo   重启服务: docker-compose restart
echo   查看状态: docker-compose ps

pause
