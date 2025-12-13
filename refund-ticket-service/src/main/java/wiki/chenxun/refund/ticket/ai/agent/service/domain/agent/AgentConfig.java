package wiki.chenxun.refund.ticket.ai.agent.service.domain.agent;

import com.alibaba.cloud.ai.agent.studio.loader.AgentLoader;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import com.alibaba.cloud.ai.graph.agent.BaseAgent;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.flow.agent.LlmRoutingAgent;
import com.alibaba.cloud.ai.graph.agent.hook.hip.HumanInTheLoopHook;
import com.alibaba.cloud.ai.graph.agent.hook.hip.ToolConfig;
import com.alibaba.cloud.ai.graph.agent.interceptor.ModelInterceptor;
import com.alibaba.cloud.ai.graph.agent.tools.WriteTodosTool;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Data;
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
import wiki.chenxun.refund.ticket.ai.agent.service.domain.model.BuyTicket;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.service.RefundTicketDomainService;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.tools.SearchTool;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.tools.SqlexecTool;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.tools.dto.SqlExecToolReq;

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
    public MemorySaver memorySaver(){
        return new MemorySaver();
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
                .description("搜索文档以查找相关数据库表结构信息")
                .inputType(String.class)
                .build();
    }

    @Bean
    public ReactAgent documentAgent(ChatModel chatModel,
                                    ToolCallback  searchTool) {
        return   ReactAgent.builder()
                .name("documentAgent")
                .model(chatModel)
                .tools(searchTool)
                .outputKey("docSearchOutPut")
                .instruction(". 你是一个票务业务文档智能助手。当需要查找信息时，使用searchTool工具. " +
                        " 基于检索到的信息回答用户的问题，并引用相关片段。")
                .saver(new MemorySaver())
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
                .inputType(SqlExecToolReq.class)
                .description("执行sql语句，返回结果,结果是json格式 ，只支持查询sql")
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
                                     ToolCallback sqlexecTool,
                                     MemorySaver memorySaver) {

      return   ReactAgent.builder()
                .name("buyTicketAgent")
                .model(chatModel)
                 .tools(currentTimeTool,sqlexecTool)
                .outputKey("buyTicketOutPut")
                .outputType(BuyTicketRes.class)
                 .instruction(" 你是一个票务业务数据分析师, 擅长基于表结构信息 ，生成对应查询sql，通过工具 currentTimeTool 获取当前时间,  通过工具 sqlexecTool 执行查询语句，返回相关结构 ")
          //       .interceptors(customTodoInterceptor)
                .saver(memorySaver)
                .build();
    }


    @Data
    public static  class BuyTicketRes {

        private List<BuyTicket> tickets;
    }


    @Bean
    public ReactAgent refundTicketAgent(ChatModel chatModel,
                                     ToolCallback refundTicketTool,
                                     HumanInTheLoopHook humanInTheLoopHook,
                                        MemorySaver memorySaver) {

        return   ReactAgent.builder()
                .name("refundTicketAgent")
                .model(chatModel)
                .tools(refundTicketTool)
                .instruction("你是一个退票操作员， 通过工具 refundTicketTool 发起退票流程 ")
                .outputKey("refundTicketOutPut")
                .outputType(String.class)
                .hooks(humanInTheLoopHook)
                //       .interceptors(customTodoInterceptor)
                .saver(memorySaver)
                .build();

    }


    @Bean
   public LlmRoutingAgent llmRoutingAgent(
           ChatModel chatModel,
           ReactAgent documentAgent,
           ReactAgent buyTicketAgent,
           MemorySaver memorySaver,
           ReactAgent refundTicketAgent){

        return LlmRoutingAgent.builder()
                .saver(memorySaver)
                .model(chatModel)
                .instruction(" 你是一个票务业务专家，负责分配具体工作到合适的专项agent , 有下列专项agent" +
                        " documentAgent 负责查询资料获取表结构数据 ，buyTicketAgent 查询购票信息 ，" +
                        " refundTicketAgent 处理退票申请 ")
                .name("llmRoutingAgent")
                .subAgents(Arrays.asList(documentAgent,buyTicketAgent,refundTicketAgent))
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
