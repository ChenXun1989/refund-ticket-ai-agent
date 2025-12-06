package wiki.chenxun.refund.ticket.ai.agent.service.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 退票记录领域模型
 *
 * @author refund-ticket
 */
@Data
@Builder
public class RefundTicket {

    /**
     * 退票ID
     */
    private Long id;

    /**
     * 购票编号
     */
    private String buyTicketCode;

    /**
     * 退票编号
     */
    private String code;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 退票状态
     */
    private RefundStatus status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 退票状态枚举
     */
    public enum RefundStatus {
        /**
         * 待处理
         */
        PENDING,
        
        /**
         * 处理中
         */
        PROCESSING,
        
        /**
         * 已通过
         */
        APPROVED,
        
        /**
         * 已拒绝
         */
        REJECTED
    }
}
