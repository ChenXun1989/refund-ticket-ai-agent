package wiki.chenxun.refund.ticket.ai.agent.application.convert;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import wiki.chenxun.refund.ticket.ai.agent.application.dto.BuyTicketDto;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.model.BuyTicket;

/**
 * @author: chenxun
 * @date: 2025/12/6
 * @version: 1.0
 * @desc
 **/
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BuyTicketDtoConvert {

    BuyTicketDto toDto(BuyTicket buyTicket);

    BuyTicket toDomain(BuyTicketDto buyTicketDto);
}
