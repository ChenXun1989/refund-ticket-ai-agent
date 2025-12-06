package wiki.chenxun.refund.ticket.ai.agent.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购票信息 DTO
 *
 * @author chenxun
 * @date 2025/12/6
 * @version 1.0
 */
@Schema(description = "购票信息")
@Data
public class BuyTicketDto {

    /**
     * 购票ID
     */
    @Schema(description = "购票ID", example = "1")
    private Long id;

    /**
     * 购票编号
     */
    @Schema(description = "购票编号", example = "T202512060001")
    private String code;

    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "张三")
    private String userName;

    /**
     * 票价
     */
    @Schema(description = "票价", example = "45.00")
    private BigDecimal ticketPrice;

    /**
     * 影院名称
     */
    @Schema(description = "影院名称", example = "万达影城")
    private String cinemaName;

    /**
     * 电影名称
     */
    @Schema(description = "电影名称", example = "流浪地球2")
    private String movieName;

    /**
     * 购票时间
     */
    @Schema(description = "购票时间", example = "2025-12-06T14:30:00")
    private LocalDateTime ticketTime;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2025-12-06T10:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2025-12-06T10:00:00")
    private LocalDateTime updateTime;
}
