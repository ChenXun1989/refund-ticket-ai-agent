# Knife4j OpenAPI 3 集成文档

## 概述

项目已成功集成 Knife4j OpenAPI 3.0，提供了美观、强大的 API 文档界面。

## 版本信息

- **Knife4j 版本**: 4.5.0
- **OpenAPI 规范**: 3.0
- **支持框架**: Spring Boot 3.4.0 + Jakarta

## 访问地址

启动应用后，可通过以下地址访问 API 文档：

- **Knife4j 文档界面**: http://localhost:8080/doc.html
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 依赖配置

### 1. 父 POM 版本管理

在 `pom.xml` 中添加了 Knife4j 版本管理：

```xml
<properties>
    <knife4j.version>4.5.0</knife4j.version>
</properties>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
            <version>${knife4j.version}</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 2. Boot 模块依赖

在 `refund-ticket-boot/pom.xml` 中添加：

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
</dependency>
```

## 配置说明

### application.yml 配置

```yaml
# Knife4j 配置
knife4j:
  enable: true
  setting:
    language: zh_cn
    enable-swagger-models: true
    enable-footer: false
    enable-footer-custom: true
    footer-custom-content: Copyright © 2025 LBX MPS AI Agent
  cors: true

# SpringDoc 配置
springdoc:
  api-docs:
    enabled: true
    path: /v3/api-docs
  swagger-ui:
    enabled: true
    path: /swagger-ui.html
  group-configs:
    - group: 'default'
      paths-to-match: '/**'
      packages-to-scan: wiki.chenxun.refund.ticket.ai.agent.application.controller
```

### OpenAPI 配置类

创建了 `OpenApiConfig.java` 配置基本信息：

```java
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("退票AI代理系统 API文档")
                        .description("基于Spring AI Alibaba的退票智能代理系统接口文档")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("ChenXun")
                                .email("chenxun@example.com")
                                .url("https://wiki.chenxun.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
```

## 注解使用示例

### Controller 注解

```java
@Tag(name = "购票管理", description = "购票信息查询相关接口")
@RestController
@RequestMapping("/buy-ticket")
public class BuyTicketController {

    @Operation(summary = "查询购票信息", description = "根据购票编号查询购票详细信息")
    @GetMapping("/info/{code}")
    public BuyTicketDto info(
            @Parameter(description = "购票编号", required = true, example = "T202512060001")
            @PathVariable("code") String code) {
        // 业务逻辑
    }
}
```

### DTO 注解

```java
@Schema(description = "购票信息")
@Data
public class BuyTicketDto {

    @Schema(description = "购票ID", example = "1")
    private Long id;

    @Schema(description = "购票编号", example = "T202512060001")
    private String code;

    @Schema(description = "用户名", example = "张三")
    private String userName;
    
    // 其他字段...
}
```

## 常用注解说明

### Controller 层注解

| 注解 | 作用 | 示例 |
|------|------|------|
| `@Tag` | 标注 Controller 分组 | `@Tag(name = "用户管理", description = "用户相关接口")` |
| `@Operation` | 描述具体的 API 操作 | `@Operation(summary = "创建用户", description = "创建新用户")` |
| `@Parameter` | 描述请求参数 | `@Parameter(description = "用户ID", required = true)` |
| `@Parameters` | 描述多个请求参数 | `@Parameters({@Parameter(...), @Parameter(...)})` |

### DTO 层注解

| 注解 | 作用 | 示例 |
|------|------|------|
| `@Schema` | 描述模型或字段 | `@Schema(description = "用户信息", example = "张三")` |
| `@ArraySchema` | 描述数组类型 | `@ArraySchema(schema = @Schema(implementation = User.class))` |

### 响应注解

| 注解 | 作用 | 示例 |
|------|------|------|
| `@ApiResponse` | 描述单个响应 | `@ApiResponse(responseCode = "200", description = "成功")` |
| `@ApiResponses` | 描述多个响应 | `@ApiResponses({@ApiResponse(...), @ApiResponse(...)})` |

## 生产环境配置

生产环境建议禁用 API 文档访问，可通过以下配置实现：

```yaml
# application-prod.yml
knife4j:
  enable: false  # 生产环境禁用
  
springdoc:
  api-docs:
    enabled: false  # 禁用 API 文档
  swagger-ui:
    enabled: false  # 禁用 Swagger UI
```

或通过 Spring Security 保护文档访问端点：

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/doc.html", "/swagger-ui/**", "/v3/api-docs/**")
            .hasRole("ADMIN")  // 仅管理员可访问
            .anyRequest().permitAll()
        );
        return http.build();
    }
}
```

## 最佳实践

### 1. 完善的注解说明

- 为每个 Controller 添加 `@Tag` 注解
- 为每个接口方法添加 `@Operation` 注解
- 为所有参数添加 `@Parameter` 注解，包括描述、是否必填、示例值
- 为所有 DTO 字段添加 `@Schema` 注解

### 2. 示例数据

在注解中提供合理的 `example` 值，帮助 API 使用者理解字段含义：

```java
@Schema(description = "手机号", example = "13800138000", pattern = "^1[3-9]\\d{9}$")
private String phone;
```

### 3. 响应状态码

明确标注可能的响应状态码：

```java
@Operation(summary = "获取用户信息")
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "成功"),
    @ApiResponse(responseCode = "404", description = "用户不存在"),
    @ApiResponse(responseCode = "500", description = "服务器错误")
})
@GetMapping("/{id}")
public User getUser(@PathVariable Long id) { }
```

### 4. 分组管理

对于大型项目，可以配置多个文档分组：

```yaml
springdoc:
  group-configs:
    - group: '用户模块'
      paths-to-match: '/api/user/**'
    - group: '订单模块'
      paths-to-match: '/api/order/**'
```

## 启动验证

1. 设置 JDK 21 环境变量：
   ```bash
   export JAVA_HOME=$(/usr/libexec/java_home -v 21)
   ```

2. 编译项目：
   ```bash
   mvn clean compile
   ```

3. 启动应用：
   ```bash
   mvn spring-boot:run -pl refund-ticket-boot
   ```

4. 访问文档：
   - 打开浏览器访问 http://localhost:8080/doc.html
   - 查看 API 接口文档和在线调试功能

## 相关资源

- [Knife4j 官方文档](https://doc.xiaominfo.com/)
- [OpenAPI 3.0 规范](https://swagger.io/specification/)
- [SpringDoc 文档](https://springdoc.org/)

## 常见问题

### 1. 文档无法访问

- 检查 `knife4j.enable` 是否为 `true`
- 确认应用已成功启动
- 检查端口是否被占用

### 2. 接口未显示在文档中

- 确认 Controller 在 `packages-to-scan` 配置的包路径下
- 检查 Controller 是否有 `@RestController` 或 `@Controller` 注解
- 确认方法有 `@RequestMapping` 相关注解

### 3. 中文乱码

- 确认 `knife4j.setting.language` 设置为 `zh_cn`
- 检查项目编码是否为 UTF-8

---

**集成完成时间**: 2025-12-06  
**集成人员**: ChenXun
