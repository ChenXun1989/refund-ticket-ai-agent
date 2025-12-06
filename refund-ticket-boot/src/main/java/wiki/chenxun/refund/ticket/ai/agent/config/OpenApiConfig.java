package wiki.chenxun.refund.ticket.ai.agent.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j OpenAPI 3 配置类
 *
 * @author chenxun
 * @date 2025/12/6
 */
@Configuration
public class OpenApiConfig {

    /**
     * OpenAPI 基本信息配置
     */
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
