package wiki.chenxun.refund.ticket.ai.agent.infra.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购票记录实体类
 *
 * @author refund-ticket
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("buy_ticket")
public class BuyTicketEntity extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
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
}
