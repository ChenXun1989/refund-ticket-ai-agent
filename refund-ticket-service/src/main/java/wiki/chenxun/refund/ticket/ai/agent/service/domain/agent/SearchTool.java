package wiki.chenxun.refund.ticket.ai.agent.service.domain.agent;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author: chenxun
 * @date: 2025/12/6
 * @version: 1.0
 * @desc
 **/
@AllArgsConstructor
public class SearchTool implements BiFunction<String, ToolContext ,String> {


    private VectorStore vectorStore;

    @Override
    public String apply(String s, ToolContext toolContext) {
        // 从向量存储检索相关文档
        List<Document> docs = vectorStore.similaritySearch(s);
        // 合并文档内容
        return docs.stream()
                .map(Document::getText)
                .collect(Collectors.joining(" "));
    }
}
