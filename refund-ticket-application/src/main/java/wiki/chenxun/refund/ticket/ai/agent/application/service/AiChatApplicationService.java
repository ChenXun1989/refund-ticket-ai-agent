package wiki.chenxun.refund.ticket.ai.agent.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * AI聊天应用服务
 * 
 * @author refund-ticket
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatApplicationService {

    private final ChatClient.Builder chatClientBuilder;

    /**
     * 与AI进行对话
     *
     * @param message 用户消息
     * @return AI响应
     */
    public String chat(String message) {
        log.info("Processing chat message: {}", message);
        
        ChatClient chatClient = chatClientBuilder.build();
        
        String response = chatClient.prompt()
                .user(message)
                .call()
                .content();
        
        log.debug("AI response: {}", response);
        return response;
    }
}
