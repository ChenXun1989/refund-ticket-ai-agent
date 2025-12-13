package wiki.chenxun.refund.ticket.ai.agent.service.domain.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.tools.dto.SqlExecToolReq;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.tools.dto.SqlExeclRes;

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
@Slf4j
@AllArgsConstructor
public class SqlexecTool implements BiFunction<SqlExecToolReq, ToolContext, SqlExeclRes> {

    private ObjectMapper objectMapper;

    private DataSource dataSource;

    @Override
    public SqlExeclRes apply(SqlExecToolReq s, ToolContext toolContext) {
        log.info("SqlexecTool apply {} ",s.getQuerySql());

       try (Connection connection= dataSource.getConnection()){
           // 执行语句返回结果
           ResultSet resultSet= connection.createStatement().executeQuery(s.getQuerySql());
           
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

           SqlExeclRes sqlExeclRes = new SqlExeclRes();
           sqlExeclRes.setJsonResult(objectMapper.writeValueAsString(resultList));
           return sqlExeclRes;
       } catch (SQLException | JsonProcessingException e) {
           throw new RuntimeException(e);
       }
    }
}
