# 退票 AI 代理系统 (Refund Ticket AI Agent)

基于 Spring Boot 3.4.0 和 Spring AI Alibaba 的多模块 Maven 项目，使用 JDK 21 开发。

## 项目信息

- **Group ID**: `wiki.chenxun`
- **Base Package**: `wiki.chenxun.refund.ticket.ai.agent`
- **JDK**: 21
- **Spring Boot**: 3.4.0
- **Spring AI Alibaba**: 1.0.0-M6.1

## 项目结构

```
refund-ticket-ai-agent/
├── refund-ticket-boot/           # 启动模块
│   └── src/main/java/wiki/chenxun/refund/ticket/ai/agent/
│       └── RefundTicketApplication.java
├── refund-ticket-application/    # 应用层模块
│   └── src/main/java/wiki/chenxun/refund/ticket/ai/agent/application/
│       ├── controller/          # REST 控制器
│       └── service/             # 应用服务
├── refund-ticket-service/        # 服务层模块（核心业务逻辑）
│   └── src/main/java/wiki/chenxun/refund/ticket/ai/agent/service/
│       └── domain/
│           ├── model/           # 领域模型
│           └── service/         # 领域服务
├── refund-ticket-infra/          # 基础设施层模块
│   └── src/main/java/wiki/chenxun/refund/ticket/ai/agent/infra/
│       └── persistence/
│           ├── entity/          # 持久化实体
│           ├── mapper/          # MapStruct 映射器
│           └── repository/      # JPA 仓储
└── refund-ticket-intergration/   # 集成层模块
    └── src/main/java/wiki/chenxun/refund/ticket/ai/agent/integration/
        ├── client/              # 外部服务客户端
        └── config/              # 配置类
```

## 模块依赖关系

```
refund-ticket-boot
    └── refund-ticket-application
            └── refund-ticket-service

refund-ticket-infra
    └── refund-ticket-service

refund-ticket-intergration
    └── refund-ticket-service
```

## 技术栈

### 核心框架
- **JDK**: 21
- **Spring Boot**: 3.4.0
- **Spring AI Alibaba**: 1.0.0-M6.1
- **Maven**: 3.8.0+

### 开发工具
- **Lombok**: 1.18.34 - 简化 Java 代码
- **MapStruct**: 1.6.2 - 对象映射

### 持久化
- **Spring Data JPA**: 数据访问层
- **H2 Database**: 开发环境数据库
- **MySQL**: 生产环境数据库

### 工具库
- **Commons Lang3**: 3.17.0
- **Guava**: 33.3.1-jre

## 快速开始

### 前置要求

1. JDK 21
2. Maven 3.8.0+

### 设置 JDK 21

```bash
# macOS/Linux
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-21
```

### 构建项目

```bash
# 清理并编译
mvn clean compile

# 打包（跳过测试）
mvn clean package -DskipTests

# 完整构建（包含测试）
mvn clean install
```

### 运行应用

```bash
# 方式 1: 使用 Maven
cd refund-ticket-boot
mvn spring-boot:run

# 方式 2: 运行 JAR
cd refund-ticket-boot/target
java -jar refund-ticket-boot-1.0.0-SNAPSHOT.jar
```

应用启动后，访问：
- 应用: http://localhost:8080
- 健康检查: http://localhost:8080/api/health
- Actuator: http://localhost:8080/actuator/health

## 配置

### Spring AI Alibaba 配置

在 `refund-ticket-boot/src/main/resources/application.yml` 中配置：

```yaml
spring:
  ai:
    alibaba:
      api-key: ${AI_API_KEY:your-api-key-here}
      chat:
        options:
          model: qwen-plus
          temperature: 0.7
          top-p: 0.9
```

### 环境变量

```bash
# 设置 API Key
export AI_API_KEY=your-actual-api-key
```

## 开发规范

### 代码风格

1. 使用 Lombok 减少样板代码
   - `@Data` - 数据类
   - `@Builder` - 构建器模式
   - `@Slf4j` - 日志
   - `@RequiredArgsConstructor` - 依赖注入

2. 使用 MapStruct 进行对象映射
   ```java
   @Mapper(componentModel = "spring")
   public interface YourMapper {
       DomainModel toEntity(EntityModel model);
   }
   ```

3. JDK 21 特性优先
   - 虚拟线程 (Virtual Threads)
   - Pattern Matching
   - Record Patterns
   - Text Blocks

### 依赖管理

- 所有依赖版本在父 POM 的 `<dependencyManagement>` 中统一管理
- 子模块不需要指定版本号
- Maven Enforcer Plugin 确保依赖版本一致性

## 项目特点

1. **分层架构**: 清晰的分层设计，职责分离
2. **DDD 设计**: 服务层采用领域驱动设计
3. **依赖管理**: 使用 Maven BOM 统一管理依赖版本
4. **代码简化**: Lombok 和 MapStruct 提高开发效率
5. **现代 Java**: 充分利用 JDK 21 新特性

## 常见问题

### 1. 构建失败：找不到 JDK 21

确保设置了正确的 JAVA_HOME：
```bash
JAVA_HOME=$(/usr/libexec/java_home -v 21) mvn clean package
```

### 2. 依赖冲突

项目使用 Maven Enforcer Plugin 检测依赖冲突。当前设置为 WARN 级别，不会中断构建。

### 3. Spring AI Alibaba 配置

确保：
1. 设置了有效的 API Key
2. 网络可以访问阿里云服务
3. 查看日志确认 AI 服务正常初始化

## 许可证

Apache License 2.0

## 作者

refund-ticket team
