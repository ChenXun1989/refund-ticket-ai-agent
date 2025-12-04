# 项目结构说明

## 项目基本信息

- **Group ID**: `wiki.chenxun`
- **Artifact ID**: `refund-ticket-ai-agent`
- **Base Package**: `wiki.chenxun.refund.ticket.ai.agent`
- **Version**: `1.0.0-SNAPSHOT`

## 包结构规范

所有模块的 Java 类都必须在 `wiki.chenxun.refund.ticket.ai.agent` 包及其子包下：

- **boot**: `wiki.chenxun.refund.ticket.ai.agent`
- **application**: `wiki.chenxun.refund.ticket.ai.agent.application`
- **service**: `wiki.chenxun.refund.ticket.ai.agent.service`
- **infra**: `wiki.chenxun.refund.ticket.ai.agent.infra`
- **integration**: `wiki.chenxun.refund.ticket.ai.agent.integration`

## 模块依赖图

根据 `.qoder/rules/design/模块.puml` 设计：

```
┌─────────────────────────────────────────────────┐
│              refund-ticket-boot                 │
│              (启动模块)                          │
└───────────────────┬─────────────────────────────┘
                    │ depends on
                    ▼
┌─────────────────────────────────────────────────┐
│         refund-ticket-application               │
│         (应用层 - 业务编排)                      │
└───────────────────┬─────────────────────────────┘
                    │ depends on
                    ▼
┌─────────────────────────────────────────────────┐
│          refund-ticket-service                  │
│          (服务层 - 核心业务逻辑)                 │
└────────┬────────────────────────────┬───────────┘
         │                            │
         │ depended by                │ depended by
         ▼                            ▼
┌──────────────────────┐    ┌──────────────────────┐
│ refund-ticket-infra  │    │refund-ticket-        │
│ (基础设施层)         │    │intergration          │
│ - 数据持久化         │    │(集成层)              │
│ - 缓存               │    │- 外部API调用         │
└──────────────────────┘    └──────────────────────┘
```

## 各模块职责

### 1. refund-ticket-boot (启动模块)
**职责**: 应用程序入口，整合所有模块

**主要内容**:
- `RefundTicketApplication.java` - Spring Boot 启动类
- `application.yml` - 应用配置
- Spring Boot 相关依赖
- 只依赖 application 模块

**关键依赖**:
```xml
<dependency>
    <groupId>com.refundticket</groupId>
    <artifactId>refund-ticket-application</artifactId>
</dependency>
```

### 2. refund-ticket-application (应用层)
**职责**: 应用服务编排，处理业务流程

**主要内容**:
- `controller/` - REST API 控制器
- `service/` - 应用服务（编排领域服务）
- `dto/` - 数据传输对象
- Spring Web 依赖
- Spring AI Alibaba 集成

**示例代码**:
```java
@Service
@RequiredArgsConstructor
public class AiChatApplicationService {
    private final ChatClient.Builder chatClientBuilder;
    
    public String chat(String message) {
        // 编排业务逻辑
    }
}
```

**关键依赖**:
- `refund-ticket-service` - 核心业务逻辑
- `spring-ai-alibaba-starter` - AI 能力

### 3. refund-ticket-service (服务层)
**职责**: 核心业务逻辑，领域驱动设计

**主要内容**:
- `domain/model/` - 领域模型
- `domain/service/` - 领域服务
- 纯业务逻辑，不依赖框架

**示例代码**:
```java
@Data
@Builder
public class RefundTicket {
    private String id;
    private String orderNo;
    private RefundStatus status;
    // ... 领域模型
}

@Service
public class RefundTicketDomainService {
    public boolean validateRefundRequest(String orderNo, String reason) {
        // 业务规则验证
    }
}
```

**关键依赖**:
- 无模块依赖（被其他模块依赖）
- 只依赖基础工具库（Lombok、Guava、Commons Lang3）

### 4. refund-ticket-infra (基础设施层)
**职责**: 技术基础设施，数据持久化

**主要内容**:
- `persistence/entity/` - JPA 实体
- `persistence/repository/` - 数据仓储
- `persistence/mapper/` - MapStruct 映射器
- Spring Data JPA 配置

**示例代码**:
```java
@Entity
@Table(name = "refund_ticket")
public class RefundTicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    // ... 持久化实体
}

@Repository
public interface RefundTicketRepository 
    extends JpaRepository<RefundTicketEntity, String> {
    List<RefundTicketEntity> findByUserId(String userId);
}

@Mapper(componentModel = "spring")
public interface RefundTicketMapper {
    RefundTicket toDomain(RefundTicketEntity entity);
    RefundTicketEntity toEntity(RefundTicket domain);
}
```

**关键依赖**:
- `refund-ticket-service` - 领域模型
- Spring Data JPA
- H2/MySQL 驱动
- MapStruct

### 5. refund-ticket-intergration (集成层)
**职责**: 外部系统集成

**主要内容**:
- `client/` - 外部服务客户端
- `config/` - WebClient 等配置
- 外部 API 调用封装

**示例代码**:
```java
@Component
@RequiredArgsConstructor
public class ExternalServiceClient {
    private final WebClient webClient;
    
    public String callExternalApi(String url) {
        // 调用外部服务
    }
}
```

**关键依赖**:
- `refund-ticket-service` - 领域模型
- Spring WebFlux (WebClient)

## 分层架构原则

### 依赖方向
```
boot → application → service ← infra
                      ↑
                      └─── intergration
```

**规则**:
1. 单向依赖：上层依赖下层，下层不依赖上层
2. service 层是核心，不依赖任何应用层模块
3. infra 和 intergration 都依赖 service，实现技术细节

### 职责划分

| 层级 | 职责 | 示例 |
|------|------|------|
| boot | 启动入口 | main 方法、配置文件 |
| application | 业务编排 | REST API、应用服务 |
| service | 核心业务 | 领域模型、业务规则 |
| infra | 技术实现 | 数据库、缓存 |
| intergration | 外部集成 | 第三方 API 调用 |

## 技术选型说明

### JDK 21 特性使用
- **Text Blocks**: 多行字符串
- **Pattern Matching**: 类型匹配
- **Record**: 不可变数据类
- **Virtual Threads**: 轻量级并发

### Lombok 使用规范
```java
// 实体类
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

// 服务类
@Service
@RequiredArgsConstructor
@Slf4j
```

### MapStruct 使用规范
```java
@Mapper(componentModel = "spring")
public interface YourMapper {
    // Spring 自动注入
    TargetDTO toDTO(SourceEntity entity);
}
```

## Maven 配置说明

### 父 POM 职责
1. 统一管理所有依赖版本
2. 配置编译插件（JDK 21）
3. 配置 Lombok 和 MapStruct 注解处理
4. Maven Enforcer 确保版本一致性

### 子模块 POM 特点
- 继承父 POM
- 不指定依赖版本号
- 只声明需要的依赖

### 编码规范

1. **包命名**: `wiki.chenxun.refund.ticket.ai.agent.模块名.分层`
2. **类命名**: 
   - Controller: `XxxController`
   - Service: `XxxService` / `XxxDomainService`
   - Repository: `XxxRepository`
   - Entity: `XxxEntity`
   - Mapper: `XxxMapper`
3. **注释**: 所有公开 API 必须有 Javadoc
4. **日志**: 使用 `@Slf4j` 统一日志

## 构建顺序

Maven Reactor 自动计算构建顺序：
1. refund-ticket-service (无依赖)
2. refund-ticket-application (依赖 service)
3. refund-ticket-infra (依赖 service)
4. refund-ticket-intergration (依赖 service)
5. refund-ticket-boot (依赖 application)

## 扩展指南

### 添加新的领域模型
1. 在 `refund-ticket-service` 中定义领域模型
2. 在 `refund-ticket-infra` 中创建对应实体和仓储
3. 在 `refund-ticket-application` 中实现应用服务
4. 在 `refund-ticket-boot` 中添加 REST API

### 集成新的外部服务
1. 在 `refund-ticket-intergration` 中创建客户端
2. 在 `refund-ticket-application` 中调用客户端
3. 遵循依赖倒置原则，在 service 层定义接口
