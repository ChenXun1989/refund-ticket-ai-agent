package wiki.chenxun.refund.ticket.ai.agent.service.domain.agent;

import com.alibaba.cloud.ai.graph.agent.interceptor.ModelCallHandler;
import com.alibaba.cloud.ai.graph.agent.interceptor.ModelInterceptor;
import com.alibaba.cloud.ai.graph.agent.interceptor.ModelRequest;
import com.alibaba.cloud.ai.graph.agent.interceptor.ModelResponse;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;

/**
 * @author: chenxun
 * @date: 2025/12/7
 * @version: 1.0
 * @desc
 **/
@AllArgsConstructor
public class CustomTodoInterceptor extends ModelInterceptor {

    private static final String DEFAULT_SYSTEM_PROMPT = """
			## `writeTodosTool`
			
			You have access to the `writeTodosTool` tool to help you manage and plan complex objectives.
			Use this tool for complex objectives to ensure that you are tracking each necessary step and giving the user visibility into your progress.
			This tool is very helpful for planning complex objectives, and for breaking down these larger complex objectives into smaller steps.
			
			It is critical that you mark todos as completed as soon as you are done with a step. Do not batch up multiple steps before marking them as completed.
			For simple objectives that only require a few steps, it is better to just complete the objective directly and NOT use this tool.
			Writing todos takes time and tokens, use it when it is helpful for managing complex many-step problems! But not for simple few-step requests.
			
			## Important To-Do List Usage Notes to Remember
			- The `writeTodosTool` tool should never be called multiple times in parallel.
			- Don't be afraid to revise the To-Do list as you go. New information may reveal new tasks that need to be done, or old tasks that are irrelevant.
			""";

    private final List<ToolCallback> tools;

    @Override
    public ModelResponse interceptModel(ModelRequest request, ModelCallHandler handler) {
        SystemMessage enhancedSystemMessage;

        if (request.getSystemMessage() == null) {
            enhancedSystemMessage = new SystemMessage(DEFAULT_SYSTEM_PROMPT);
        } else {
            enhancedSystemMessage = new SystemMessage(request.getSystemMessage().getText() + "\n\n" + DEFAULT_SYSTEM_PROMPT);
        }

        // Create enhanced request
        ModelRequest enhancedRequest = ModelRequest.builder(request)
                .systemMessage(enhancedSystemMessage)
                .build();

        // Call the handler with enhanced request
        return handler.call(enhancedRequest);
    }

    @Override
    public String getName() {
        return "customTodoInterceptor";
    }

    @Override
    public List<ToolCallback> getTools() {
        return tools;
    }
}
