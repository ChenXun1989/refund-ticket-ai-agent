package wiki.chenxun.refund.ticket.ai.agent.infra.persistence.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 退票状态枚举
 *
 * @author refund-ticket
 */
@Getter
public enum RefundTicketStatus {

    /**
     * 待处理
     */
    PENDING("PENDING", "待处理"),

    /**
     * 处理中
     */
    PROCESSING("PROCESSING", "处理中"),

    /**
     * 已通过
     */
    APPROVED("APPROVED", "已通过"),

    /**
     * 已拒绝
     */
    REJECTED("REJECTED", "已拒绝");

    /**
     * 状态码（存储到数据库的值）
     */
    @EnumValue
    private final String code;

    /**
     * 状态描述
     */
    private final String description;

    RefundTicketStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
