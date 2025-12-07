package wiki.chenxun.refund.ticket.ai.agent.service.domain.agent;

import com.alibaba.cloud.ai.agent.studio.loader.AgentLoader;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import com.alibaba.cloud.ai.graph.agent.BaseAgent;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.flow.agent.SequentialAgent;
import com.alibaba.cloud.ai.graph.agent.hook.hip.HumanInTheLoopHook;
import com.alibaba.cloud.ai.graph.agent.hook.hip.ToolConfig;
import com.alibaba.cloud.ai.graph.agent.interceptor.ModelInterceptor;
import com.alibaba.cloud.ai.graph.agent.interceptor.todolist.TodoListInterceptor;
import com.alibaba.cloud.ai.graph.agent.tools.WriteTodosTool;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.service.RefundTicketDomainService;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * @author: chenxun
 * @date: 2025/12/6
 * @version: 1.0
 * @desc
 **/
@Slf4j
@Configuration
public class AgentConfig {


    @PostConstruct
    public void init(){
        log.info("AgentConfig init");
    }

    @Bean
    public VectorStore vectorStore(DashScopeEmbeddingModel dashScopeEmbeddingModel) {

        // 读取文件
        Resource resource = new ClassPathResource("database-schema.txt");
        TextReader textReader = new TextReader(resource);
        List<Document> documents = textReader.get();

        // 2. 分割文档为块
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> chunks = splitter.apply(documents);

        VectorStore vectorStore = SimpleVectorStore.builder(dashScopeEmbeddingModel)
                .build();
// 3. 将块添加到向量存储
        vectorStore.add(chunks);

        return vectorStore;


    }

    @Bean
    public ToolCallback searchTool(VectorStore vectorStore){
        SearchTool searchTool = new SearchTool(vectorStore);
        return FunctionToolCallback.builder("searchTool", searchTool)
                .description("搜索数据库表结构信息")
                .inputType(String.class)
                .build();
    }




    @Bean
    public ToolCallback currentTimeTool(){
        return FunctionToolCallback
                .builder("currentTimeTool",()-> LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .description("获取当前时间,返回格式 yyyy-MM-dd HH:mm:ss")
                .build();
    }

    @Bean
    public  ToolCallback sqlexecTool(DataSource dataSource,
                                     ObjectMapper objectMapper){
        SqlexecTool sqlexecTool = new SqlexecTool(objectMapper,dataSource);

        return FunctionToolCallback
                .builder("sqlexecTool",sqlexecTool)
                .description("执行sql语句，返回结果,结果是json格式")
                .inputType(String.class)
                .build();
    }

    @Bean
    public ToolCallback refundTicketTool(RefundTicketDomainService ticketDomainService){
       return  FunctionToolCallback
                .builder("refundTicketTool",ticketDomainService::refundTicket)
                .description(" 退票申请,返回true表示申请成功，false表示申请失败")
                .inputType(String.class)
                .build();
    }


    @Bean
    public ToolCallback writeTodosTool(){
        return FunctionToolCallback
                .builder("writeTodosTool",new WriteTodosTool())
                .description(" 规划代办事项 ")
                .inputType(WriteTodosTool.Request.class)
                .build();
    }

    @Bean
    public ModelInterceptor customTodoInterceptor(ToolCallback writeTodosTool){
        return new CustomTodoInterceptor(Arrays.asList(writeTodosTool));

    }


    @Bean
    public HumanInTheLoopHook acceptHook(){
        return HumanInTheLoopHook.builder()
                .approvalOn("refundTicketTool", ToolConfig.builder()
                        .description("退票执行需要确认是否是这个购票编号")
                        .build())
                .build();
    }


    @Bean
    public ReactAgent buyTicketAgent(ChatModel chatModel,ToolCallback currentTimeTool,
                                     ToolCallback  searchTool,
                                     ToolCallback sqlexecTool,
                                     ToolCallback refundTicketTool,
                                     ModelInterceptor customTodoInterceptor,
                                     HumanInTheLoopHook humanInTheLoopHook) {

      return   ReactAgent.builder()
                .systemPrompt(" 你是一个退票业务专家，获取当前时间, 擅长通过查询资料库，生成对应sql查询和分析数据, 以及发起退票申请流程 " )
                .name("buyTicketAgent")
                .model(chatModel)
                 .tools(currentTimeTool,searchTool,sqlexecTool,refundTicketTool)
                .outputKey("buyTicketOutPut")
                .outputType(String.class)
                .hooks(humanInTheLoopHook)
          //       .interceptors(customTodoInterceptor)
                .saver(new MemorySaver())
                .build();

    }


    @Bean
    public SequentialAgent sequentialAgent(ReactAgent buyTicketAgent) throws GraphStateException {
        return SequentialAgent.builder()
                .subAgents(Arrays.asList(buyTicketAgent))
                .description(" 客服智能体，识别用户意图，分配到对应agent")
                .name("sequentialAgent")
                .build();
    }



    @Bean
    public AgentLoader agentLoader(ReactAgent buyTicketAgent){

        return new AgentLoader(){

            @NotNull
            @Override
            public List<String> listAgents() {
                return List.of(buyTicketAgent.name());
            }

            @Override
            public BaseAgent loadAgent(String name) {
                return buyTicketAgent;
            }
        };
    }




}
