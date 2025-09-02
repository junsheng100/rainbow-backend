#!/bin/bash

# Rainbow Backend Docker 部署脚本
# 作者: jackson.liu
# 版本: 1.0.0

set -e

echo "=========================================="
echo "    Rainbow Backend Docker 部署脚本"
echo "=========================================="

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_message() {
    local color=$1
    local message=$2
    echo -e "${color}${message}${NC}"
}

# 检查 Docker 环境
check_docker() {
    print_message $BLUE "🔍 检查 Docker 环境..."
    
    if ! command -v docker &> /dev/null; then
        print_message $RED "❌ 错误: 未找到 Docker，请先安装 Docker"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        print_message $RED "❌ 错误: 未找到 Docker Compose，请先安装 Docker Compose"
        exit 1
    fi
    
    # 检查 Docker 服务状态
    if ! docker info &> /dev/null; then
        print_message $RED "❌ 错误: Docker 服务未启动，请先启动 Docker 服务"
        exit 1
    fi
    
    print_message $GREEN "✅ Docker 环境检查通过"
}

# 创建必要的目录
create_directories() {
    print_message $BLUE "📁 创建必要的目录..."
    
    mkdir -p logs upload docker/config
    mkdir -p docker/mysql/{init,conf}
    mkdir -p docker/redis
    mkdir -p docker/nginx/{conf.d,ssl}
    
    print_message $GREEN "✅ 目录创建完成"
}

# 生成自签名 SSL 证书
generate_ssl_cert() {
    print_message $BLUE "🔐 生成自签名 SSL 证书..."
    
    if [ ! -f "docker/nginx/ssl/rainbow-backend.crt" ]; then
        mkdir -p docker/nginx/ssl
        
        # 生成自签名证书
        openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
            -keyout docker/nginx/ssl/rainbow-backend.key \
            -out docker/nginx/ssl/rainbow-backend.crt \
            -subj "/C=CN/ST=Beijing/L=Beijing/O=Rainbow/OU=IT/CN=localhost"
        
        print_message $GREEN "✅ SSL 证书生成完成"
    else
        print_message $YELLOW "⚠️  SSL 证书已存在，跳过生成"
    fi
}

# 构建镜像
build_images() {
    print_message $BLUE "🔨 构建 Docker 镜像..."
    
    # 停止现有容器
    docker-compose down 2>/dev/null || true
    
    # 构建镜像
    docker-compose build --no-cache
    
    print_message $GREEN "✅ 镜像构建完成"
}

# 启动服务
start_services() {
    print_message $BLUE "🚀 启动服务..."
    
    # 启动基础服务（MySQL、Redis）
    docker-compose up -d mysql redis
    
    print_message $YELLOW "⏳ 等待数据库服务启动..."
    sleep 30
    
    # 启动 kkFileView 文件预览服务
    docker-compose up -d kkfileview
    
    print_message $YELLOW "⏳ 等待文件预览服务启动..."
    sleep 15
    
    # 启动应用服务
    docker-compose up -d rainbow-backend
    
    print_message $YELLOW "⏳ 等待应用服务启动..."
    sleep 20
    
    # 启动 Nginx（可选）
    if [ "$1" = "--with-nginx" ]; then
        print_message $BLUE "🌐 启动 Nginx 服务..."
        docker-compose --profile nginx up -d nginx
    fi
    
    print_message $GREEN "✅ 服务启动完成"
}

# 检查服务状态
check_services() {
    print_message $BLUE "🔍 检查服务状态..."
    
    echo ""
    docker-compose ps
    
    echo ""
    print_message $BLUE "📊 服务健康状态:"
    
    # 检查应用健康状态
    if curl -f http://localhost:8080/actuator/health &>/dev/null; then
        print_message $GREEN "✅ 应用服务运行正常"
    else
        print_message $RED "❌ 应用服务异常"
    fi
    
    # 检查数据库连接
    if docker-compose exec -T mysql mysqladmin ping -h localhost -u root -proot123 &>/dev/null; then
        print_message $GREEN "✅ MySQL 服务运行正常"
    else
        print_message $RED "❌ MySQL 服务异常"
    fi
    
    # 检查 Redis 连接
    if docker-compose exec -T redis redis-cli -a redis123 ping &>/dev/null; then
        print_message $GREEN "✅ Redis 服务运行正常"
    else
        print_message $RED "❌ Redis 服务异常"
    fi
    
    # 检查 kkFileView 服务
    if curl -f http://localhost:8012/onlinePreview &>/dev/null; then
        print_message $GREEN "✅ kkFileView 文件预览服务运行正常"
    else
        print_message $RED "❌ kkFileView 文件预览服务异常"
    fi
}

# 显示访问信息
show_access_info() {
    print_message $GREEN "🎉 部署完成！"
    echo ""
    print_message $BLUE "📱 访问信息:"
    echo "  应用地址: http://localhost:8080"
    echo "  API文档: http://localhost:8080/swagger-ui.html"
    echo "  健康检查: http://localhost:8080/actuator/health"
    echo "  文件预览: http://localhost:8012"
    echo ""
    
    if [ "$1" = "--with-nginx" ]; then
        print_message $BLUE "🌐 Nginx 代理:"
        echo "  HTTP: http://localhost (自动重定向到 HTTPS)"
        echo "  HTTPS: https://localhost"
        echo ""
    fi
    
    print_message $BLUE "🔧 管理命令:"
    echo "  查看日志: docker-compose logs -f"
    echo "  停止服务: docker-compose down"
    echo "  重启服务: docker-compose restart"
    echo "  查看状态: docker-compose ps"
}

# 主函数
main() {
    local with_nginx=false
    
    # 解析参数
    while [[ $# -gt 0 ]]; do
        case $1 in
            --with-nginx)
                with_nginx=true
                shift
                ;;
            --help|-h)
                echo "用法: $0 [选项]"
                echo "选项:"
                echo "  --with-nginx    同时启动 Nginx 反向代理"
                echo "  --help, -h      显示帮助信息"
                exit 0
                ;;
            *)
                print_message $RED "❌ 未知参数: $1"
                echo "使用 --help 查看帮助信息"
                exit 1
                ;;
        esac
    done
    
    check_docker
    create_directories
    generate_ssl_cert
    build_images
    
    if [ "$with_nginx" = true ]; then
        start_services --with-nginx
        check_services
        show_access_info --with-nginx
    else
        start_services
        check_services
        show_access_info
    fi
}

# 错误处理
trap 'print_message $RED "\n❌ 部署过程中发生错误，请检查日志"; exit 1' ERR

# 执行主函数
main "$@"
