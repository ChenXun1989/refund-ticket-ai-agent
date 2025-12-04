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
    private String id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 退票原因
     */
    private String reason;

    /**
     * 退票状态
     */
    private RefundStatus status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

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
         * 已完成
         */
        COMPLETED,
        
        /**
         * 已拒绝
         */
        REJECTED
    }
}
