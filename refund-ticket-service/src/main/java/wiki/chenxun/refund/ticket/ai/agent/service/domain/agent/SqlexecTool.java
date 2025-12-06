package wiki.chenxun.refund.ticket.ai.agent.service.domain.agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.model.ToolContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author: chenxun
 * @date: 2025/12/6
 * @version: 1.0
 * @desc
 **/
@AllArgsConstructor
public class SqlexecTool implements BiFunction<String, ToolContext,String> {

    private ObjectMapper objectMapper;

    private DataSource dataSource;

    @Override
    public String apply(String s, ToolContext toolContext) {

       try (Connection connection= dataSource.getConnection()){
           // 执行语句返回结果
           ResultSet resultSet= connection.createStatement().executeQuery(s);
           
           // 解析 ResultSet 转成 JSON 字符串
           List<Map<String, Object>> resultList = new ArrayList<>();
           ResultSetMetaData metaData = resultSet.getMetaData();
           int columnCount = metaData.getColumnCount();
           
           while (resultSet.next()) {
               Map<String, Object> row = new HashMap<>();
               for (int i = 1; i <= columnCount; i++) {
                   String columnName = metaData.getColumnLabel(i);
                   Object value = resultSet.getObject(i);
                   row.put(columnName, value);
               }
               resultList.add(row);
           }
           
           return objectMapper.writeValueAsString(resultList);
       } catch (SQLException | JsonProcessingException e) {
           throw new RuntimeException(e);
       }
    }
}
