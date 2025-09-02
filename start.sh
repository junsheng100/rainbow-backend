#!/bin/bash

# Rainbow Backend 项目启动脚本
# 作者: jackson.liu
# 版本: 1.0.0

echo "=========================================="
echo "    Rainbow Backend 项目启动脚本"
echo "=========================================="

# 检查Java环境
check_java() {
    if ! command -v java &> /dev/null; then
        echo "❌ 错误: 未找到Java环境，请先安装JDK 1.8+"
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1,2)
    echo "✅ Java版本: $JAVA_VERSION"
    
    if [[ "$JAVA_VERSION" < "1.8" ]]; then
        echo "❌ 错误: Java版本过低，需要JDK 1.8+"
        exit 1
    fi
}

# 检查Maven环境
check_maven() {
    if ! command -v mvn &> /dev/null; then
        echo "❌ 错误: 未找到Maven环境，请先安装Maven 3.6+"
        exit 1
    fi
    
    MAVEN_VERSION=$(mvn -version | head -n 1 | cut -d' ' -f3)
    echo "✅ Maven版本: $MAVEN_VERSION"
}

# 检查数据库连接
check_database() {
    echo "🔍 检查数据库连接..."
    
    # 读取配置文件中的数据库信息
    DB_CONFIG_FILE="rainbow-server/rainbow-system/src/main/resources/application-dev.yml"
    
    if [ ! -f "$DB_CONFIG_FILE" ]; then
        echo "❌ 错误: 配置文件不存在: $DB_CONFIG_FILE"
        exit 1
    fi
    
    # 提取数据库URL
    DB_URL=$(grep "url:" "$DB_CONFIG_FILE" | head -1 | sed 's/.*url: //' | tr -d '"')
    
    if [ -z "$DB_URL" ]; then
        echo "❌ 错误: 无法读取数据库配置"
        exit 1
    fi
    
    echo "✅ 数据库配置: $DB_URL"
}

# 编译项目
build_project() {
    echo "🔨 开始编译项目..."
    
    if mvn clean install -Dmaven.test.skip=true; then
        echo "✅ 项目编译成功"
    else
        echo "❌ 项目编译失败"
        exit 1
    fi
}

# 启动应用
start_application() {
    echo "🚀 启动应用..."
    
    # 检查端口是否被占用
    PORT=8080
    if lsof -Pi :$PORT -sTCP:LISTEN -t >/dev/null ; then
        echo "⚠️  警告: 端口 $PORT 已被占用"
        read -p "是否继续启动? (y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            echo "❌ 启动已取消"
            exit 1
        fi
    fi
    
    echo "✅ 启动应用，端口: $PORT"
    echo "📱 应用地址: http://localhost:$PORT"
    echo "📚 API文档: http://localhost:$PORT/swagger-ui.html"
    echo ""
    echo "按 Ctrl+C 停止应用"
    echo ""
    
    # 启动应用
    mvn spring-boot:run -pl rainbow-server/rainbow-system
}

# 主函数
main() {
    echo "🔍 检查环境..."
    check_java
    check_maven
    check_database
    
    echo ""
    echo "🔨 编译项目..."
    build_project
    
    echo ""
    echo "🚀 启动应用..."
    start_application
}

# 错误处理
error_handler() {
    echo ""
    echo "❌ 启动过程中发生错误"
    echo "请检查以下内容:"
    echo "1. Java环境是否正确安装 (JDK 1.8+)"
    echo "2. Maven环境是否正确安装 (3.6+)"
    echo "3. 数据库服务是否启动"
    echo "4. 配置文件是否正确"
    echo "5. 端口是否被占用"
    exit 1
}

# 设置错误处理
trap error_handler ERR

# 执行主函数
main
