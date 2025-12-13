package wiki.chenxun.refund.ticket.ai.agent.service.domain.agent;

import com.alibaba.cloud.ai.graph.*;
import com.alibaba.cloud.ai.graph.action.AsyncNodeAction;
import com.alibaba.cloud.ai.graph.checkpoint.config.SaverConfig;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.state.strategy.AppendStrategy;
import com.alibaba.cloud.ai.graph.state.strategy.MergeStrategy;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.agent.node.ClassifyIntentNode;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.agent.node.LookupCustomerHistory;

import java.util.HashMap;
import java.util.Map;

import static com.alibaba.cloud.ai.graph.action.AsyncEdgeAction.edge_async;

/**
 * @author: chenxun
 * @date: 2025/12/13
 * @version: 1.0
 * @desc
 **/
@Configuration
public class GraphConfig {

    @Resource
    private ClassifyIntentNode classifyIntentNode;

    @Resource
    private LookupCustomerHistory lookupCustomerHistory;

    @Resource
    private MemorySaver memorySaver;

    @Bean
    public CompiledGraph compiledGraph() throws GraphStateException {


        // 和 共享状态有关


        // 配置 KeyStrategyFactory
        KeyStrategyFactory keyStrategyFactory = () -> {
            HashMap<String, KeyStrategy> keyStrategyHashMap = new HashMap<>();
            keyStrategyHashMap.put("userInput", new ReplaceStrategy());
            keyStrategyHashMap.put("userTxt", new AppendStrategy());
            keyStrategyHashMap.put("nextNode", new ReplaceStrategy());
            keyStrategyHashMap.put("interruptionMessage", new ReplaceStrategy());
            keyStrategyHashMap.put("userName", new ReplaceStrategy());
            return keyStrategyHashMap;
        };


        var stateGraph=new StateGraph(keyStrategyFactory);

        stateGraph.addNode("classifyIntentNode", AsyncNodeAction.node_async(classifyIntentNode));
        stateGraph.addNode("lookupCustomerHistory", AsyncNodeAction.node_async(lookupCustomerHistory));

        stateGraph.addEdge(StateGraph.START, "classifyIntentNode");

        stateGraph.addConditionalEdges("classifyIntentNode",
                edge_async(state -> {
                    return (String) state.value("nextNode").orElse("lookupCustomerHistory");
                }),
                Map.of(
                        "lookupCustomerHistory", "lookupCustomerHistory",
                        "classifyIntentNode","classifyIntentNode"
                ));


      //  stateGraph.addEdge("classifyIntentNode", "lookupCustomerHistory");
        stateGraph.addEdge("lookupCustomerHistory", StateGraph.END);


        var compileConfig = CompileConfig.builder()
                .saverConfig(SaverConfig.builder()
                        .register(memorySaver)
                        .build())
                .interruptBefore("lookupCustomerHistory")
                .build();

        return stateGraph.compile(compileConfig);
    }
}
