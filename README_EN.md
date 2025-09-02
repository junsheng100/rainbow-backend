## 📚 Documentation Center

Rainbow Backend provides a complete documentation system to help users of different roles quickly get started and use the system.

> 📖 **Document Index**: [README_INDEX.md](README_INDEX.md) - Quickly find required documents

### 📖 Documentation Navigation

- **[Project Technical Summary](document/PROJECT_SUMMARY.md)** - Technical architecture, core module analysis, technical features
- **[Development Guide](document/DEVELOPMENT_GUIDE.md)** - Development standards, security development, performance optimization, testing guide
- **[Quick Start Guide](document/QUICK_START.md)** - 5-minute quick start, environment check, core functionality experience
- **[Docker Deployment Guide](docker/docs/DOCKER_DEPLOY.md)** - Complete containerized deployment instructions

### 🎯 Choose Documents by Role

- **New Users**: Start with [Quick Start Guide](document/QUICK_START.md)
- **Developers**: Focus on [Development Guide](document/DEVELOPMENT_GUIDE.md)
- **Architects**: Check [Project Technical Summary](document/PROJECT_SUMMARY.md)
- **Operations Staff**: Reference [Docker Deployment Guide](docker/docs/DOCKER_DEPLOY.md)

---

## 🔧 Deployment Guide

### 🐳 Docker Deployment (Recommended)

Rainbow Backend provides complete Docker deployment support, including one-click deployment scripts and detailed configuration instructions.

#### 🚀 Quick Deployment

```bash
# Enter Docker directory
cd docker

# One-click deployment (Linux/macOS)
./scripts/docker-deploy.sh

# One-click deployment (Windows)
scripts\docker-deploy.bat

# Manual deployment
docker-compose up -d
```

#### 📁 Docker Directory Structure

```
docker/
├── README.md                    # Docker documentation
├── Dockerfile                   # Application image build file
├── docker-compose.yml           # Service orchestration configuration
├── scripts/                     # Deployment scripts
├── docs/                        # Deployment documentation
├── config/                      # Application configuration
├── mysql/                       # MySQL configuration
├── redis/                       # Redis configuration
└── nginx/                       # Nginx configuration
```

#### 🔧 Detailed Configuration

- **Docker Deployment Guide**: [docker/docs/DOCKER_DEPLOY.md](docker/docs/DOCKER_DEPLOY.md)
- **Docker Directory Description**: [docker/README.md](docker/README.md)

More deployment-related documentation can be found at: [📚 Documentation Center](#documentation-center)

### Traditional Deployment

#### 1. **Build Image**

```dockerfile
# Dockerfile
FROM openjdk:8-jre-alpine
VOLUME /tmp
COPY target/rainbow-system.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

#### 2. **Docker Compose**

```yaml
# docker-compose.yml
version: '3.8'
services:
  rainbow-system:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    depends_on:
      - mysql
      - redis
  
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: db_rainbow
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
  
  redis:
    image: redis:6-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data

volumes:
  mysql_data:
  redis_data:
```

## 📊 Monitoring and Operations

### System Monitoring

#### 1. **Performance Monitoring**

- CPU usage monitoring
- Memory usage monitoring
- JVM heap memory monitoring
- Database connection pool monitoring

#### 2. **Business Monitoring**

- User login statistics
- Interface call frequency
- Exception statistics
- System response time

#### 3. **Security Monitoring**

- Login failure count
- Abnormal access detection
- Sensitive operation records
- Permission change audit

## 🌟 Technical Features

### 🔧 Architecture Features

#### 1. **Layered Architecture Design**

- **Four-layer architecture**: Controller → Service → Dao → Repository
- **Separation of responsibilities**: Clear responsibilities for each layer, easy to maintain and test
- **Template method pattern**: Provide common functionality through base classes, reduce duplicate code
- **Dependency injection**: Use Spring IoC container to manage object lifecycle

#### 2. **Security Protection System**

- **Multi-layer security protection**: JWT + Spring Security + custom filters
- **XSS protection**: Input/output filtering and escaping
- **SQL injection protection**: Parameterized queries and SQL injection detection
- **Duplicate submission protection**: Redis-based anti-duplicate submission mechanism
- **Operation log audit**: Complete operation records and audit tracking

#### 3. **Performance Optimization Strategy**

- **Caching mechanism**: Redis cache hot data, improve response speed
- **Connection pool optimization**: Database connection pool and thread pool configuration optimization
- **Pagination queries**: Avoid large data queries, improve query efficiency
- **Batch operations**: Support batch insert, update, delete operations

### 🎯 Business Features

#### 1. **Intelligent Task Scheduling**

- **Quartz integration**: Distributed task scheduling based on Quartz
- **Dynamic task management**: Support dynamic creation, modification, deletion of tasks
- **Task monitoring**: Real-time monitoring of task execution status and results
- **Failure retry**: Support automatic retry mechanism for task failures

#### 2. **System Monitoring Center**

- **Real-time monitoring**: CPU, memory, disk, JVM real-time monitoring
- **Performance analysis**: System performance indicator analysis and trend prediction
- **Alert mechanism**: Automatic alerting and notification for abnormal situations
- **Visualization display**: Visual chart display of monitoring data

#### 3. **API Documentation Management**

- **Automatic generation**: API documentation automatic generation based on Swagger
- **Online debugging**: Support API online debugging and testing
- **Version management**: API version management and change tracking
- **Permission control**: API access permission control and management

#### 4. **File Preview System**

- **Multi-format support**: Based on [kkFileView](https://github.com/kekingcn/kkFileView) supporting Office, PDF, images and other formats
- **Online preview**: Preview file content directly in browser without downloading
- **Format conversion**: Support file format conversion and online viewing
- **Security control**: File access permission control and preview log recording

## 🤝 Contributing Guide

### Development Process

1. **Fork project**
2. **Create feature branch**: `git checkout -b feature/your-feature`
3. **Commit changes**: `git commit -am 'Add some feature'`
4. **Push branch**: `git push origin feature/your-feature`
5. **Create Pull Request**

### Code Review

- All code must pass code review
- Follow project code standards
- Add necessary test cases
- Update related documentation

## 📄 License

This project is licensed under the [MIT License](LICENSE).

## 📞 Contact Us

- **Project Address**: https://github.com/junsheng100/rainbow-backend
- **Issue Feedback**: https://github.com/junsheng100/rainbow-backend/issues
- **Email**: junsheng100@foxmail.com
- **QQ**: 304299340

## 📈 Project Summary

### 🎯 Project Value

Rainbow Backend is an enterprise-level, feature-complete backend system solution with the following core values:

#### 1. **Technical Advancement**

- Adopts the latest Spring Boot 2.7.12 technology stack
- Integrates AI-assisted development tools to improve development efficiency
- Uses microservice architecture to support high concurrency and large-scale deployment
- Complete security protection system to ensure system security

#### 2. **Feature Completeness**

- Complete user authentication and permission management system
- Intelligent task scheduling and system monitoring functionality
- Flexible template management and API documentation system
- Powerful data migration and synchronization tools

#### 3. **Architectural Rationality**

- Clear layered architecture with clear separation of responsibilities
- Modular design, easy to maintain and expand
- Unified design patterns and development standards
- Complete exception handling and log recording

#### 4. **Practicality**

- Ready to use, quick deployment
- Detailed documentation and example code
- Complete development tools and standards
- Active community support and maintenance updates

### 🚀 Future Plans

#### 1. **Technical Upgrades**

- Upgrade to Spring Boot 3.x and Java 17
- Integrate more AI-assisted development features
- Support cloud-native deployment and containerization
- Enhance microservice governance capabilities

#### 2. **Feature Expansion**

- Support more authentication methods (OAuth2, SAML, LDAP)
- Enhance permission management functionality (dynamic permissions, permission inheritance)
- Improve monitoring and alerting systems
- Add more business modules

#### 3. **Performance Optimization**

- Introduce distributed caching and message queues
- Optimize database queries and indexes
- Support read-write separation and database sharding
- Enhance system fault tolerance and degradation capabilities

#### 4. **Ecosystem Building**

- Improve development documentation and tutorials
- Build developer community
- Provide more examples and best practices
- Support plugin expansion

### 💡 Usage Recommendations

#### 1. **Development Teams**

- Strictly follow project code standards and architectural design
- Fully utilize base classes and utility classes provided by the project
- Regularly update dependency versions to maintain technology stack advancement
- Establish complete testing and deployment processes

#### 2. **Operations Teams**

- Reasonably configure system parameters and optimize performance
- Establish complete monitoring and alerting mechanisms
- Regularly backup data and configuration files
- Develop emergency plans and fault recovery procedures

#### 3. **Business Teams**

- Reasonably design permission models based on business requirements
- Fully utilize system monitoring and logging functionality
- Regularly conduct security audits and permission cleanup
- Pay attention to system performance indicators and user experience

---

<div align="center">

**If this project helps you, please give it a ⭐️**

Made with ❤️ by jackson.liu

</div>
