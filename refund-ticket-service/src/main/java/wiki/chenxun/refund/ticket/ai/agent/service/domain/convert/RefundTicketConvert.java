package wiki.chenxun.refund.ticket.ai.agent.service.domain.convert;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import wiki.chenxun.refund.ticket.ai.agent.infra.persistence.entity.RefundTicketEntity;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.model.RefundTicket;

/**
 * 退票记录对象映射器
 * 使用 MapStruct 进行领域模型和持久化实体之间的转换
 *
 * @author refund-ticket
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RefundTicketConvert {

    /**
     * 实体转领域模型
     *
     * @param entity 实体
     * @return 领域模型
     */
    RefundTicket toDomain(RefundTicketEntity entity);

    /**
     * 领域模型转实体
     *
     * @param domain 领域模型
     * @return 实体
     */
    RefundTicketEntity toEntity(RefundTicket domain);
}
