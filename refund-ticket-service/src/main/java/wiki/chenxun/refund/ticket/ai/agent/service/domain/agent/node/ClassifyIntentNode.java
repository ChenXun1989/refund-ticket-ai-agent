package wiki.chenxun.refund.ticket.ai.agent.service.domain.agent.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.StructuredOutputValidationAdvisor;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.agent.node.dto.UserClassifyDto;

import java.util.Map;

/**
 * @author: chenxun
 * @date: 2025/12/10
 * @version: 1.0
 * @desc
 **/
@Slf4j
@Service
public class ClassifyIntentNode implements NodeAction {

    @Resource
    private ChatModel chatModel;

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {


      //  log.info("====== 追加文本=======   {}" , state.value("userTxt",Object.class).orElse(""));

        var validationAdvisor = StructuredOutputValidationAdvisor.builder()
                .outputType(UserClassifyDto.class)
                .maxRepeatAttempts(3)
                .advisorOrder(BaseAdvisor.HIGHEST_PRECEDENCE + 1000)
                .build();

        var chartClient = ChatClient.builder(chatModel)
                .defaultAdvisors(validationAdvisor)
                .build();

        // 用户输入的数据
        UserMessage input=  state.value("userInput", UserMessage.class)
                .orElseThrow(()->new RuntimeException("用户输入为空"));

        String userTxt= input.getText();


        ChatClient.CallResponseSpec callResponseSpec = chartClient.prompt(" 分析用户输入内容 ，提取意图和用户名称，意图为以下几种 queryTicketLog , refundTicket  ." +
                " 用户输入内容如下 "  + userTxt).call();

        UserClassifyDto classifyDto= callResponseSpec.responseEntity(UserClassifyDto.class)
                .entity();

        boolean hasUserName= StringUtils.hasText(classifyDto.getUserName());

        return Map.of("classification",classifyDto.getClassification(),
                "userName",classifyDto.getUserName(),
                "nextNode", hasUserName?
                "END":"lookupCustomerHistory",
                "interruptionMessage",hasUserName?"":"请输入用户名称",
                "userTxt",userTxt);
    }

}
