package wiki.chenxun.refund.ticket.ai.agent.integration.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 外部服务客户端
 *
 * @author refund-ticket
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalServiceClient {

    private final WebClient webClient;

    /**
     * 调用外部API
     *
     * @param url 外部服务URL
     * @return 响应结果
     */
    public String callExternalApi(String url) {
        log.info("Calling external API: {}", url);
        
        try {
            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Error calling external API: {}", url, e);
            throw new RuntimeException("Failed to call external API", e);
        }
    }
}
