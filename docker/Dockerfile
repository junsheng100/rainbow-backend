# Rainbow Backend Dockerfile
# 作者: jackson.liu
# 版本: 1.0.0

# 使用 OpenJDK 8 作为基础镜像
FROM openjdk:8-jre-alpine

# 设置维护者信息
LABEL maintainer="jackson.liu <junsheng100@foxmail.com>"
LABEL description="Rainbow Backend - 企业级后端系统"
LABEL version="1.0.0"

# 设置工作目录
WORKDIR /app

# 设置时区为中国时区
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apk del tzdata

# 创建应用用户
RUN addgroup -g 1000 rainbow && \
    adduser -D -s /bin/sh -u 1000 -G rainbow rainbow

# 创建必要的目录
RUN mkdir -p /app/logs /app/upload /app/config && \
    chown -R rainbow:rainbow /app

# 复制 JAR 文件
COPY target/rainbow-system.jar app.jar

# 复制配置文件（可选，用于生产环境配置覆盖）
COPY docker/config/ /app/config/

# 设置文件权限
RUN chown rainbow:rainbow app.jar && \
    chmod +x app.jar

# 切换到应用用户
USER rainbow

# 暴露端口
EXPOSE 8080

# 设置 JVM 参数
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 设置应用参数
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# 启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=$SPRING_PROFILES_ACTIVE --server.port=$SERVER_PORT"]
