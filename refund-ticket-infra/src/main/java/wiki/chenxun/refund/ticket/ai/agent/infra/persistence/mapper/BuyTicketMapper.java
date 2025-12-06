package wiki.chenxun.refund.ticket.ai.agent.infra.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import wiki.chenxun.refund.ticket.ai.agent.infra.persistence.entity.BuyTicketEntity;

/**
 * 购票记录 Mapper
 *
 * @author refund-ticket
 */
@Mapper
public interface BuyTicketMapper extends BaseMapper<BuyTicketEntity> {
}
