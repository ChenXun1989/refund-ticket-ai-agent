---
trigger: always_on
alwaysApply: true
---

# 基础规则

## 项目环境
- JDK版本: 21
- 请确保所有代码使用JDK 21的特性和语法
- 在编写代码时，优先考虑使用JDK 21的新特性，如：
  - Virtual Threads (虚拟线程)
  - Pattern Matching for switch
  - Record Patterns
  - String Templates (预览特性)
  - Sequenced Collections

## 编码规范
- 遵循Java编码规范
- 使用有意义的变量名和方法名
- 添加必要的注释和文档
- 保持代码简洁和可读性

## 依赖管理
- 使用Maven或Gradle进行依赖管理

- 保持依赖版本的一致性和最新性
- 使用Maven Enforcer Plugin强制管理依赖版本，确保整个项目中每个依赖只有一个版本
- 在pom.xml中配置maven-enforcer-plugin，添加DependencyConvergence规则来检测依赖冲突


## 代码简化工具
- 使用Lombok简化Java代码
  - 使用@Data、@Builder等注解减少样板代码
  - 使用@Slf4j进行日志记录
  - 使用@RequiredArgsConstructor进行依赖注入
  - 避免过度使用@Data，根据需要选择@Getter、@Setter等更精确的注解
  
- 使用MapStruct进行对象映射
  - 定义Mapper接口进行DTO和Entity之间的转换
  - 使用@Mapping注解处理字段映射
  - 配置componentModel = "spring"以便Spring自动注入
  - 优先使用MapStruct而不是手动编写转换代码，提高性能和可维护性



## API文档
- 使用Knife4j OpenAPI 3生成和展示API文档
  - 在pom.xml中添加knife4j-openapi3-jakarta-spring-boot-starter依赖
  - 配置OpenAPI基本信息，包括标题、版本、描述、联系人等
  - 使用@Tag注解标注Controller类，描述接口分组
  - 使用@Operation注解描述具体的API操作
  - 使用@Parameter注解描述请求参数
  - 使用@Schema注解描述DTO字段信息
  - 访问路径：http://localhost:port/doc.html
  - 生产环境建议通过配置禁用文档访问


## 配置管理
- 使用YAML文件管理应用配置
  - 配置文件命名规范：application.yml（主配置）、application-{profile}.yml（环境配置）
  - 使用@ConfigurationProperties注解绑定配置类
  - 配置类使用@Data或@Getter注解简化代码
  - 避免在代码中硬编码配置值
  - 敏感信息（如密码、密钥）使用环境变量或配置中心管理
  - 配置文件结构要清晰，使用合理的层级关系
  - 为配置类添加@Validated注解进行参数校验
  - 使用JSR-349注解（如@NotNull、@Min等）验证配置参数的有效性



## 参数校验
- 对外接口使用JSR-349注解进行参数校验
  - 在Controller方法参数上添加@Valid或@Validated注解启用校验
  - 在请求对象（DTO）字段上使用校验注解：
    - @NotNull：不能为null
    - @NotEmpty：不能为空（用于集合、字符串）
    - @NotBlank：不能为空白（用于字符串）
    - @Size(min, max)：限制字符串长度或集合大小
    - @Min/@Max：限制数值范围
    - @Pattern：正则表达式校验
    - @Email：邮箱格式校验
    - @Past/@Future：日期时间校验
  - 使用@Validated注解支持分组校验
  - 在Controller中使用BindingResult或@ExceptionHandler统一处理校验异常
  - 自定义校验注解时实现ConstraintValidator接口
  - 校验错误信息使用message属性自定义，支持国际化



## 应用监控
- 使用Spring Boot Actuator进行应用监控和管理
  - 在pom.xml中添加spring-boot-starter-actuator依赖
  - 配置暴露的端点：
    management:
      endpoints:
        web:
          exposure:
            include: health,info,metrics,prometheus
      endpoint:
        health:
          show-details: always
      metrics:
        export:
          prometheus:
            enabled: true
  - 常用端点说明：
    - /actuator/health：健康检查端点
    - /actuator/info：应用信息端点
    - /actuator/metrics：应用指标端点
    - /actuator/prometheus：Prometheus格式的指标端点
  - 生产环境注意事项：
    - 使用Spring Security保护敏感端点
    - 仅暴露必要的端点
    - 配置management.server.port使用独立端口
    - 关闭不需要的端点以减少安全风险
  - 自定义健康检查：
    - 实现HealthIndicator接口创建自定义健康检查
    - 使用@Component注解注册健康检查组件
  - 自定义指标：
    - 注入MeterRegistry收集自定义业务指标
    - 使用Counter、Gauge、Timer等记录指标数据
