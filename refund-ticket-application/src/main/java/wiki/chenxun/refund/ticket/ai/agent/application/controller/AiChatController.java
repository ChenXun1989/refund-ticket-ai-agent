package wiki.chenxun.refund.ticket.ai.agent.application.controller;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.flow.agent.SequentialAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author: chenxun
 * @date: 2025/12/6
 * @version: 1.0
 * @desc
 **/
@Validated
@RestController
@RequestMapping("test/ai")
public class AiChatController {


    @Resource(name="buyTicketAgent")
    private ReactAgent reactAgent;

    @Resource
    private SequentialAgent sequentialAgent;

    @GetMapping("/chat")
    public String chat(@RequestParam("question") String question,
                       @RequestParam("bizId") String bizId ){
        RunnableConfig runnableConfig=RunnableConfig.builder()
                        .threadId(bizId).build();
        try {
            Optional<OverAllState> overAllState = sequentialAgent.invoke(question,runnableConfig);
            if(overAllState.isPresent()){
                Optional<AssistantMessage> assistantMessage= overAllState.get().value("buyTicketOutPut", AssistantMessage.class);
                return assistantMessage.map(AssistantMessage::getText)
                        .orElse("不支持此类问题");
            }
            return "不支持此类问题";
//            AssistantMessage assistantMessage = reactAgent.call(question,runnableConfig);
//            return assistantMessage.getText();
        } catch (GraphRunnerException e) {
            throw new RuntimeException(e);
        }
    }
}
