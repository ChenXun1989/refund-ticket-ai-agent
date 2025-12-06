# 退票 AI 代理系统 (Refund Ticket AI Agent)

基于 Spring Boot 3.4.0 和 Spring AI Alibaba 的智能退票代理系统，使用 JDK 21 开发。该系统通过 AI 智能体技术，实现电影票购买和退票流程的自动化处理。

## 项目信息

- **Group ID**: `wiki.chenxun`
- **Artifact ID**: `refund-ticket-ai-agent`
- **Base Package**: `wiki.chenxun.refund.ticket.ai.agent`
- **Version**: 1.0.0-SNAPSHOT
- **JDK**: 21
- **Spring Boot**: 3.4.0
- **Spring AI Alibaba**: 1.1.0.0-M5

## 项目结构

```
refund-ticket-ai-agent/
├── refund-ticket-boot/           # 启动模块
│   ├── src/main/java/wiki/chenxun/refund/ticket/ai/agent/
│   │   ├── config/              # 配置类（OpenAPI、Studio）
│   │   └── RefundTicketApplication.java
│   └── src/main/resources/
│       ├── application.yml      # 应用配置
│       ├── mysql-init.sql       # 数据库初始化脚本
│       └── database-schema.txt  # 数据库结构文档
├── refund-ticket-application/    # 应用层模块
│   └── src/main/java/wiki/chenxun/refund/ticket/ai/agent/application/
│       ├── controller/          # REST 控制器
│       ├── service/             # 应用服务
│       ├── dto/                 # 数据传输对象
│       └── convert/             # DTO转换器
├── refund-ticket-service/        # 服务层模块（核心业务逻辑）
│   └── src/main/java/wiki/chenxun/refund/ticket/ai/agent/service/domain/
│       ├── model/               # 领域模型
│       ├── service/             # 领域服务
│       ├── convert/             # 领域对象转换器
│       └── agent/               # AI Agent 配置与工具
├── refund-ticket-infra/          # 基础设施层模块
│   └── src/main/java/wiki/chenxun/refund/ticket/ai/agent/infra/
│       ├── config/              # MyBatis Plus 配置
│       └── persistence/
│           ├── entity/          # 持久化实体
│           └── mapper/          # MyBatis Mapper
└── refund-ticket-intergration/   # 集成层模块
    └── src/main/java/wiki/chenxun/refund/ticket/ai/agent/integration/
        ├── client/              # 外部服务客户端
        └── config/              # WebClient 配置
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

## 核心功能

### 1. 购票管理
- 购票信息查询
- 购票记录存储
- 支持多影院、多电影场次

### 2. 退票管理
- 退票申请提交
- 退票状态跟踪（待处理、处理中、已通过、已拒绝）
- 退票记录查询

### 3. AI 智能交互
- 基于 Spring AI Alibaba 的智能对话
- Agent 工具集成（SearchTool、SqlexecTool）
- 支持通义千问模型

### 4. API 文档
- Knife4j 文档界面：http://localhost:8080/doc.html
- Swagger UI：http://localhost:8080/swagger-ui.html
- OpenAPI 3.0 规范

## 技术栈

### 核心框架
- **JDK**: 21
- **Spring Boot**: 3.4.0
- **Spring AI Alibaba**: 1.1.0.0-M5
- **Maven**: 3.8.0+

### 开发工具
- **Lombok**: 1.18.42 - 简化 Java 代码
- **MapStruct**: 1.6.2 - 对象映射
- **Knife4j**: 4.5.0 - API 文档

### 持久化
- **MyBatis Plus**: 3.5.9 - ORM 框架
- **MySQL**: 8.0+ - 数据库

### 工具库
- **Commons Lang3**: 3.17.0
- **Guava**: 33.3.1-jre
- **Jakarta Validation**: 3.1.0 - 参数校验

## 快速开始

### 前置要求

1. **JDK 21**
2. **Maven 3.8.0+**
3. **MySQL 8.0+**
4. **阿里云 DashScope API Key**（用于 AI 功能）

### 环境准备

#### 1. 设置 JDK 21

```bash
# macOS/Linux
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH=$JAVA_HOME/bin:$PATH

# 验证
java -version  # 应显示 java version "21.x.x"
```

#### 2. 初始化数据库

```bash
# 使用 MySQL 客户端执行初始化脚本
mysql -u root -p < refund-ticket-boot/src/main/resources/mysql-init.sql
```

#### 3. 配置 API Key

在 `refund-ticket-boot/src/main/resources/application.yml` 中配置：

```yaml
spring:
  ai:
    dashscope:
      api-key: your-dashscope-api-key  # 替换为你的 API Key
```

或通过环境变量设置：

```bash
export SPRING_AI_DASHSCOPE_API_KEY=your-dashscope-api-key
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

应用启动后，访问以下地址：

- **API 文档**: http://localhost:8080/doc.html（Knife4j 界面）
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **健康检查**: http://localhost:8080/actuator/health
- **应用信息**: http://localhost:8080/actuator/info

## 配置说明

### 数据库配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/refund_ticket
    username: root
    password: your-password  # 修改为你的数据库密码
```

### Spring AI 配置

```yaml
spring:
  ai:
    dashscope:
      api-key: your-api-key  # 阿里云 DashScope API Key
      chat:
        options:
          model: qwen-plus      # 使用通义千问 Plus 模型
          temperature: 0.7      # 控制回复的随机性
          top-p: 0.9           # 控制回复的多样性
```

### MyBatis Plus 配置

```yaml
mybatis-plus:
  type-aliases-package: wiki.chenxun.refund.ticket.ai.agent.infra.persistence.entity
  global-config:
    db-config:
      id-type: auto           # 主键自增
      logic-delete-field: deleted
  configuration:
    map-underscore-to-camel-case: true  # 驼峰命名转换
```

### Knife4j 配置

```yaml
knife4j:
  enable: true                # 启用 Knife4j
  setting:
    language: zh_cn          # 中文界面
    enable-swagger-models: true
```

## 数据库设计

### 购票记录表 (buy_ticket)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| code | VARCHAR(64) | 购票编号（唯一）|
| user_name | VARCHAR(100) | 用户名 |
| ticket_price | DECIMAL(10,2) | 票价 |
| cinema_name | VARCHAR(200) | 影院名称 |
| movie_name | VARCHAR(200) | 电影名称 |
| ticket_time | DATETIME | 购票时间 |

### 退票记录表 (refund_ticket)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| buy_ticket_code | VARCHAR(64) | 关联购票编号 |
| code | VARCHAR(64) | 退票编号（唯一）|
| user_name | VARCHAR(100) | 用户名 |
| status | VARCHAR(20) | 退票状态 |

**退票状态枚举**:
- `PENDING`: 待处理
- `PROCESSING`: 处理中
- `APPROVED`: 已通过
- `REJECTED`: 已拒绝

详细的数据库结构说明请参考：[database-schema.txt](refund-ticket-boot/src/main/resources/database-schema.txt)

## API 接口

### 购票管理

- `GET /buy-ticket/info/{code}` - 查询购票信息

### AI 对话

- `POST /ai/chat` - AI 智能对话接口

### 健康检查

- `GET /health` - 应用健康状态
- `GET /actuator/health` - Spring Boot Actuator 健康检查

完整的 API 文档请访问：http://localhost:8080/doc.html

## 开发规范

### 代码风格

1. 使用 Lombok 减少样板代码
   - `@Data` - 数据类
   - `@Builder` - 构建器模式
   - `@Slf4j` - 日志
   - `@RequiredArgsConstructor` - 依赖注入

2. **使用 MapStruct 进行对象映射**
   ```java
   @Mapper(componentModel = "spring")
   public interface YourConvert {
       TargetDTO toDTO(SourceEntity entity);
   }
   ```

3. **参数校验**
   - 使用 JSR-349 注解（`@NotNull`、`@NotBlank`、`@Valid`）
   - 在 Controller 方法参数上添加 `@Valid` 注解

4. **API 文档注解**
   - Controller 类：`@Tag(name = "模块名", description = "描述")`
   - 接口方法：`@Operation(summary = "摘要", description = "详细描述")`
   - 参数：`@Parameter(description = "说明", example = "示例")`
   - DTO 字段：`@Schema(description = "说明", example = "示例")`

### 依赖管理

- 所有依赖版本在父 POM 的 `<dependencyManagement>` 中统一管理
- 子模块不需要指定版本号
- Maven Enforcer Plugin 强制依赖版本一致性
- 使用 BOM 管理 Spring Boot 和 Spring AI Alibaba 依赖

## 项目特点

### 架构设计
1. **多模块分层架构**: Boot、Application、Service、Infra、Integration 清晰分层
2. **DDD 设计**: 服务层采用领域驱动设计，领域模型和领域服务分离
3. **依赖倒置**: 上层依赖下层，下层不依赖上层

### 技术特性
4. **Spring AI 集成**: 基于 Spring AI Alibaba 的智能代理能力
5. **AI Agent 工具**: 内置 SearchTool 和 SqlexecTool
6. **MyBatis Plus**: 强大的 ORM 框架，自动填充、逻辑删除等功能
7. **API 文档自动化**: Knife4j + OpenAPI 3.0 自动生成美观的 API 文档

### 开发效率
8. **代码简化**: Lombok 减少样板代码
9. **对象映射**: MapStruct 自动生成类型安全的映射代码
10. **参数校验**: JSR-349 声明式参数校验
11. **现代 Java**: 充分利用 JDK 21 新特性

### 质量保障
12. **依赖管理**: Maven Enforcer 强制依赖版本一致性
13. **应用监控**: Spring Boot Actuator 提供健康检查和指标监控

## 常见问题

### 1. 构建失败：找不到 JDK 21

**原因**: 系统默认 JDK 不是 21

**解决方案**:
```bash
# 显式设置 JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH=$JAVA_HOME/bin:$PATH

# 验证
java -version

# 构建项目
mvn clean compile
```

### 2. MapStruct 生成的转换器未注入

**原因**: Lombok 和 MapStruct 注解处理器顺序问题

**解决方案**: 父 POM 已配置正确的注解处理器顺序，先 Lombok 后 MapStruct

### 3. 数据库连接失败

**原因**: MySQL 未启动或配置错误

**解决方案**:
```bash
# 确保 MySQL 已启动
mysql.server start

# 创建数据库
mysql -u root -p < refund-ticket-boot/src/main/resources/mysql-init.sql

# 检查 application.yml 中的数据库配置
```

### 4. API Key 未配置

**错误信息**: `dashscope.api-key is required`

**解决方案**:
```yaml
# 在 application.yml 中配置
spring:
  ai:
    dashscope:
      api-key: your-actual-api-key
```

### 5. Knife4j 文档访问 404

**原因**: 配置未生效或包扫描路径错误

**解决方案**: 确认 `application.yml` 中的配置：
```yaml
knife4j:
  enable: true
springdoc:
  packages-to-scan: wiki.chenxun.refund.ticket.ai.agent.application.controller
```

### 6. 依赖冲突构建失败

**原因**: Maven Enforcer Plugin 检测到依赖版本冲突

**解决方案**: 父 POM 已统一管理所有依赖版本，确保使用最新的 pom.xml

## 相关文档

- [项目结构说明](PROJECT_STRUCTURE.md) - 详细的模块依赖和分层架构说明
- [Knife4j 集成文档](KNIFE4J_INTEGRATION.md) - API 文档工具集成指南
- [数据库结构文档](refund-ticket-boot/src/main/resources/database-schema.txt) - AI 友好的数据库设计文档

## 技术支持

- **Spring AI Alibaba 官方文档**: https://sca.aliyun.com/ai/
- **Knife4j 官方文档**: https://doc.xiaominfo.com/
- **MyBatis Plus 官方文档**: https://baomidou.com/

## 许可证

Apache License 2.0

## 作者

ChenXun - chenxun@example.com

---
## 社群
![IMG_0223.JPG](IMG_0223.JPG)   


**最后更新**: 2025-12-06
