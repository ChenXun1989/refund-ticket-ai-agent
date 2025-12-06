# 项目结构说明

## 项目基本信息

- **Group ID**: `wiki.chenxun`
- **Artifact ID**: `refund-ticket-ai-agent`
- **Base Package**: `wiki.chenxun.refund.ticket.ai.agent`
- **Version**: `1.0.0-SNAPSHOT`
- **JDK**: 21
- **Spring Boot**: 3.4.0
- **Spring AI Alibaba**: 1.1.0.0-M5

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
- `config/` - 配置类（OpenApiConfig、StudioConfig）
- `application.yml` - 应用配置
- `mysql-init.sql` - 数据库初始化脚本
- `database-schema.txt` - AI 友好的数据库结构文档
- Spring Boot 相关依赖
- 只依赖 application 模块

**关键依赖**:
```xml
<dependency>
    <groupId>wiki.chenxun</groupId>
    <artifactId>refund-ticket-application</artifactId>
</dependency>
```

### 2. refund-ticket-application (应用层)
**职责**: 应用服务编排，处理业务流程

**主要内容**:
- `controller/` - REST API 控制器
  - `HealthController` - 健康检查
  - `BuyTicketController` - 购票管理
  - `AiChatController` - AI 对话
- `service/` - 应用服务（编排领域服务）
  - `AiChatApplicationService` - AI 聊天应用服务
- `dto/` - 数据传输对象
  - `BuyTicketDto` - 购票信息 DTO
- `convert/` - DTO 转换器
  - `BuyTicketDtoConvert` - MapStruct 转换器
- Spring Web 依赖
- Spring AI Alibaba 集成
- Knife4j API 文档

**示例代码**:
```java
@Tag(name = "购票管理", description = "购票信息查询相关接口")
@RestController
@RequestMapping("/buy-ticket")
@RequiredArgsConstructor
public class BuyTicketController {
    private final BuyTicketDomainService buyTicketDomainService;
    
    @Operation(summary = "查询购票信息")
    public BuyTicketDto info(@PathVariable String code) {
        // 业务逻辑
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
  - `BuyTicket` - 购票领域模型
  - `RefundTicket` - 退票领域模型
- `domain/service/` - 领域服务
  - `BuyTicketDomainService` - 购票领域服务
  - `RefundTicketDomainService` - 退票领域服务
- `domain/convert/` - 领域对象转换器
  - `BuyTicketConvert` - MapStruct 转换器
  - `RefundTicketConvert` - MapStruct 转换器
- `domain/agent/` - AI Agent 配置与工具
  - `AgentConfig` - Agent 配置
  - `SearchTool` - 搜索工具
  - `SqlexecTool` - SQL 执行工具
- 纯业务逻辑，不依赖框架

**示例代码**:
```java
@Data
@Builder
public class BuyTicket {
    private Long id;
    private String code;
    private String userName;
    private BigDecimal ticketPrice;
    private String cinemaName;
    private String movieName;
    private LocalDateTime ticketTime;
}

@Service
@RequiredArgsConstructor
public class BuyTicketDomainService {
    private final BuyTicketMapper buyTicketMapper;
    
    public BuyTicket getByCode(String code) {
        // 领域业务逻辑
    }
}
```

**关键依赖**:
- 无模块依赖（被其他模块依赖）
- 只依赖基础工具库（Lombok、Guava、Commons Lang3）

### 4. refund-ticket-infra (基础设施层)
**职责**: 技术基础设施，数据持久化

**主要内容**:
- `config/` - 配置类
  - `MyBatisPlusMetaObjectHandler` - 字段自动填充配置
- `persistence/entity/` - 持久化实体
  - `BaseEntity` - 基础实体
  - `BuyTicketEntity` - 购票实体
  - `RefundTicketEntity` - 退票实体
  - `RefundTicketStatus` - 退票状态枚举
- `persistence/mapper/` - MyBatis Mapper
  - `BuyTicketMapper` - 购票 Mapper
  - `RefundTicketMapper` - 退票 Mapper
- MyBatis Plus 配置

**示例代码**:
```java
@Data
@TableName("buy_ticket")
public class BuyTicketEntity extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String code;
    private String userName;
    private BigDecimal ticketPrice;
    private String cinemaName;
    private String movieName;
    private LocalDateTime ticketTime;
}

@Mapper
public interface BuyTicketMapper extends BaseMapper<BuyTicketEntity> {
    // MyBatis Plus 自动提供 CRUD 方法
}
```

**关键依赖**:
- `refund-ticket-service` - 领域模型
- MyBatis Plus
- MySQL 驱动
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

### 核心框架
- **JDK 21**: 利用现代 Java 特性
- **Spring Boot 3.4.0**: 最新的 Spring 生态
- **Spring AI Alibaba 1.1.0.0-M5**: AI 智能体能力

### 持久化框架
- **MyBatis Plus 3.5.9**: 强大的 ORM 框架
  - 自动填充 create_time、update_time
  - 逻辑删除支持
  - 丰富的 CRUD API
  - 内置分页插件

### 开发工具
- **Lombok 1.18.42**: 简化 Java 代码
- **MapStruct 1.6.2**: 类型安全的对象映射
- **Knife4j 4.5.0**: API 文档工具

### 工具库
- **Commons Lang3 3.17.0**: 常用工具类
- **Guava 33.3.1-jre**: Google 核心库
- **Jakarta Validation 3.1.0**: 参数校验

### Lombok 使用规范
```java
// 实体类/DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyTicketDto {
    // 字段
}

// 服务类
@Service
@RequiredArgsConstructor
@Slf4j
public class BuyTicketDomainService {
    private final BuyTicketMapper buyTicketMapper;
}
```

### MapStruct 使用规范
```java
@Mapper(componentModel = "spring")
public interface BuyTicketConvert {
    // Entity -> Domain
    BuyTicket toDomain(BuyTicketEntity entity);
    
    // Domain -> Entity
    BuyTicketEntity toEntity(BuyTicket domain);
    
    // 列表转换
    List<BuyTicket> toDomainList(List<BuyTicketEntity> entities);
}
```

**注意事项**:
- MapStruct 接口命名使用 `Convert` 后缀，避免与 MyBatis `Mapper` 冲突
- MyBatis Mapper 接口使用 `Mapper` 后缀，标注 `@Mapper` 注解
- Spring 会自动注入 MapStruct 生成的实现类

## Maven 配置说明

### 父 POM 职责
1. 统一管理所有依赖版本（使用 `<dependencyManagement>`）
2. 配置编译插件（JDK 21）
3. 配置 Lombok 和 MapStruct 注解处理器（正确顺序）
4. Maven Enforcer 强制依赖版本一致性
5. 使用 BOM 管理 Spring Boot 和 Spring AI Alibaba 依赖

### 子模块 POM 特点
- 继承父 POM
- 不指定依赖版本号
- 只声明需要的依赖

### 注解处理器配置
```xml
<annotationProcessorPaths>
    <!-- 先 Lombok，后 MapStruct -->
    <path>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok.version}</version>
    </path>
    <path>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct-processor</artifactId>
        <version>${mapstruct.version}</version>
    </path>
</annotationProcessorPaths>
```

### 编码规范

1. **包命名**: `wiki.chenxun.refund.ticket.ai.agent.模块名.分层`
2. **类命名**: 
   - Controller: `XxxController`
   - Service: `XxxService` / `XxxDomainService`
   - Mapper (数据库): `XxxMapper`
   - Convert (MapStruct): `XxxConvert`
   - Entity: `XxxEntity`
   - DTO: `XxxDto`
3. **注释**: 所有公开 API 必须有 Javadoc 和 Knife4j 注解
4. **日志**: 使用 `@Slf4j` 统一日志
5. **参数校验**: 使用 JSR-349 注解（`@NotNull`、`@NotBlank`、`@Valid`）

## 构建顺序

Maven Reactor 自动计算构建顺序：
1. refund-ticket-service (无依赖)
2. refund-ticket-application (依赖 service)
3. refund-ticket-infra (依赖 service)
4. refund-ticket-intergration (依赖 service)
5. refund-ticket-boot (依赖 application)

## 扩展指南

### 添加新的领域模型
1. 在 `refund-ticket-service/domain/model` 中定义领域模型
2. 在 `refund-ticket-infra/persistence/entity` 中创建对应实体
3. 在 `refund-ticket-infra/persistence/mapper` 中创建 MyBatis Mapper
4. 在 `refund-ticket-service/domain/convert` 中创建 MapStruct 转换器
5. 在 `refund-ticket-service/domain/service` 中实现领域服务
6. 在 `refund-ticket-application` 中实现应用服务和 REST API

### 集成新的外部服务
1. 在 `refund-ticket-intergration/client` 中创建客户端
2. 在 `refund-ticket-application` 中调用客户端
3. 遵循依赖倒置原则，在 service 层定义接口

### 添加 AI Agent 工具
1. 在 `refund-ticket-service/domain/agent` 中创建工具类
2. 实现 `java.util.function.Function` 接口
3. 在 `AgentConfig` 中注册工具
4. 使用 `@Description` 注解描述工具功能

## 最佳实践

### 1. API 文档注解
```java
@Tag(name = "模块名", description = "模块描述")
@RestController
public class XxxController {
    
    @Operation(summary = "接口摘要", description = "详细描述")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功"),
        @ApiResponse(responseCode = "404", description = "未找到")
    })
    public ResponseEntity<XxxDto> method(
        @Parameter(description = "参数说明", example = "示例值")
        @PathVariable String param) {
        // 业务逻辑
    }
}
```

### 2. DTO 字段注解
```java
@Schema(description = "购票信息")
@Data
public class BuyTicketDto {
    @Schema(description = "购票编号", example = "BT20231201001")
    private String code;
    
    @Schema(description = "票价", example = "58.00")
    private BigDecimal ticketPrice;
}
```

### 3. 参数校验
```java
@Data
public class CreateRequest {
    @NotBlank(message = "用户名不能为空")
    private String userName;
    
    @NotNull(message = "票价不能为空")
    @Min(value = 0, message = "票价必须大于0")
    private BigDecimal ticketPrice;
}
```

### 4. MyBatis Plus 自动填充
```java
@Component
public class MyBatisPlusMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
    
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
```

## 相关文档

- [README.md](README.md) - 项目概览和快速开始
- [KNIFE4J_INTEGRATION.md](KNIFE4J_INTEGRATION.md) - API 文档工具集成指南
- [database-schema.txt](refund-ticket-boot/src/main/resources/database-schema.txt) - AI 友好的数据库结构文档

---

**最后更新**: 2025-12-06
