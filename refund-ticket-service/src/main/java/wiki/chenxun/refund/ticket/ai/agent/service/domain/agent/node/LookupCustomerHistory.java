package wiki.chenxun.refund.ticket.ai.agent.service.domain.agent.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author: chenxun
 * @date: 2025/12/13
 * @version: 1.0
 * @desc
 **/

@Service
public class LookupCustomerHistory implements NodeAction {



    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        String customerId = state.value("userName")
                .map(v -> (String) v)
                .orElse(null);

        //。


        // 现在继续查找
        return Map.of(
                "next_node", "END"
        );
    }

}
