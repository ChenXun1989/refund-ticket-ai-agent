# 项目更新说明

## 更新时间
2025-12-04

## 更新内容

### 1. GroupId 更新
- **旧值**: `com.refundticket`
- **新值**: `wiki.chenxun`

所有 POM 文件的 groupId 已统一更新为 `wiki.chenxun`。

### 2. 包结构重构
所有 Java 类已迁移到新的包结构：

**旧包结构**:
```
com.refundticket
├── application
├── service
├── infra
└── integration
```

**新包结构**:
```
wiki.chenxun.refund.ticket.ai.agent
├── application
├── service
├── infra
└── integration
```

### 3. 详细包路径映射

| 模块 | 旧包路径 | 新包路径 |
|------|---------|---------|
| boot | `com.refundticket` | `wiki.chenxun.refund.ticket.ai.agent` |
| application | `com.refundticket.application` | `wiki.chenxun.refund.ticket.ai.agent.application` |
| service | `com.refundticket.service` | `wiki.chenxun.refund.ticket.ai.agent.service` |
| infra | `com.refundticket.infra` | `wiki.chenxun.refund.ticket.ai.agent.infra` |
| integration | `com.refundticket.integration` | `wiki.chenxun.refund.ticket.ai.agent.integration` |

### 4. 影响的文件

#### POM 文件 (6个)
- `pom.xml` (父 POM)
- `refund-ticket-boot/pom.xml`
- `refund-ticket-application/pom.xml`
- `refund-ticket-service/pom.xml`
- `refund-ticket-infra/pom.xml`
- `refund-ticket-intergration/pom.xml`

#### Java 源文件 (10个)
- `RefundTicketApplication.java`
- `HealthController.java`
- `AiChatApplicationService.java`
- `RefundTicket.java`
- `RefundTicketDomainService.java`
- `RefundTicketEntity.java`
- `RefundTicketRepository.java`
- `RefundTicketMapper.java`
- `WebClientConfig.java`
- `ExternalServiceClient.java`

#### 文档文件 (2个)
- `README.md`
- `PROJECT_STRUCTURE.md`

### 5. 构建验证

✅ 所有模块编译通过
```
[INFO] Refund Ticket AI Agent ............................. SUCCESS
[INFO] Refund Ticket Service .............................. SUCCESS
[INFO] Refund Ticket Application .......................... SUCCESS
[INFO] Refund Ticket Boot ................................. SUCCESS
[INFO] Refund Ticket Infrastructure ....................... SUCCESS
[INFO] Refund Ticket Integration .......................... SUCCESS
[INFO] BUILD SUCCESS
```

### 6. 包结构规范

所有 Java 类必须严格遵守以下包结构规范：

1. **基础包**: `wiki.chenxun.refund.ticket.ai.agent`
2. **各模块包**:
   - boot: 直接在基础包下
   - application: 基础包 + `.application.[controller|service|...]`
   - service: 基础包 + `.service.domain.[model|service]`
   - infra: 基础包 + `.infra.[persistence|...]`
   - integration: 基础包 + `.integration.[client|config]`

### 7. 开发注意事项

#### 新增类的包路径
创建新类时，必须遵循以下包路径：

```java
// 启动类
package wiki.chenxun.refund.ticket.ai.agent;

// 应用层控制器
package wiki.chenxun.refund.ticket.ai.agent.application.controller;

// 应用层服务
package wiki.chenxun.refund.ticket.ai.agent.application.service;

// 领域模型
package wiki.chenxun.refund.ticket.ai.agent.service.domain.model;

// 领域服务
package wiki.chenxun.refund.ticket.ai.agent.service.domain.service;

// 持久化实体
package wiki.chenxun.refund.ticket.ai.agent.infra.persistence.entity;

// 仓储接口
package wiki.chenxun.refund.ticket.ai.agent.infra.persistence.repository;

// MapStruct 映射器
package wiki.chenxun.refund.ticket.ai.agent.infra.persistence.mapper;

// 集成层客户端
package wiki.chenxun.refund.ticket.ai.agent.integration.client;

// 集成层配置
package wiki.chenxun.refund.ticket.ai.agent.integration.config;
```

#### 依赖声明
在 POM 文件中声明项目内部模块依赖时，使用新的 groupId：

```xml
<dependency>
    <groupId>wiki.chenxun</groupId>
    <artifactId>refund-ticket-service</artifactId>
</dependency>
```

### 8. 后续工作

- [x] 更新所有 POM 文件的 groupId
- [x] 重构所有 Java 类到新包结构
- [x] 更新项目文档
- [x] 验证构建成功
- [ ] 更新单元测试（如需要）
- [ ] 更新集成测试（如需要）
- [ ] 更新部署脚本（如需要）

## 兼容性说明

**破坏性变更**: 是

此次更新涉及包结构的完全重构，与之前的版本不兼容。如果有其他项目依赖本项目，需要同步更新依赖配置。

## 回滚方案

如需回滚到旧的包结构：
1. 恢复 POM 文件中的 groupId 为 `com.refundticket`
2. 将所有 Java 类移回 `com.refundticket` 包下
3. 重新编译项目

## 联系方式

如有问题，请联系项目维护团队。
