package wiki.chenxun.refund.ticket.ai.agent.service.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购票记录领域模型
 *
 * @author refund-ticket
 */
@Data
@Builder
public class BuyTicket {

    /**
     * 购票ID
     */
    private Long id;

    /**
     * 购票编号
     */
    private String code;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 票价
     */
    private BigDecimal ticketPrice;

    /**
     * 影院名称
     */
    private String cinemaName;

    /**
     * 电影名称
     */
    private String movieName;

    /**
     * 购票时间
     */
    private LocalDateTime ticketTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
