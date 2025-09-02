@echo off
chcp 65001 >nul

echo ==========================================
echo     Rainbow Backend 项目启动脚本
echo ==========================================

REM 检查Java环境
echo 🔍 检查Java环境...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 错误: 未找到Java环境，请先安装JDK 1.8+
    pause
    exit /b 1
)

for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VERSION=%%g
)
echo ✅ Java版本: %JAVA_VERSION%

REM 检查Maven环境
echo 🔍 检查Maven环境...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 错误: 未找到Maven环境，请先安装Maven 3.6+
    pause
    exit /b 1
)

for /f "tokens=3" %%g in ('mvn -version ^| findstr /i "Apache Maven"') do (
    set MAVEN_VERSION=%%g
)
echo ✅ Maven版本: %MAVEN_VERSION%

REM 检查配置文件
echo 🔍 检查配置文件...
if not exist "rainbow-server\rainbow-system\src\main\resources\application-dev.yml" (
    echo ❌ 错误: 配置文件不存在
    pause
    exit /b 1
)
echo ✅ 配置文件存在

REM 编译项目
echo.
echo 🔨 开始编译项目...
call mvn clean install -Dmaven.test.skip=true
if %errorlevel% neq 0 (
    echo ❌ 项目编译失败
    pause
    exit /b 1
)
echo ✅ 项目编译成功

REM 启动应用
echo.
echo 🚀 启动应用...
echo ✅ 启动应用，端口: 8080
echo 📱 应用地址: http://localhost:8080
echo 📚 API文档: http://localhost:8080/swagger-ui.html
echo.
echo 按 Ctrl+C 停止应用
echo.

REM 启动应用
call mvn spring-boot:run -pl rainbow-server/rainbow-system

pause
