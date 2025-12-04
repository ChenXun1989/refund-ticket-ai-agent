package wiki.chenxun.refund.ticket.ai.agent.infra.persistence.repository;

import wiki.chenxun.refund.ticket.ai.agent.infra.persistence.entity.RefundTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 退票记录仓储接口
 *
 * @author refund-ticket
 */
@Repository
public interface RefundTicketRepository extends JpaRepository<RefundTicketEntity, String> {

    /**
     * 根据用户ID查询退票记录
     *
     * @param userId 用户ID
     * @return 退票记录列表
     */
    List<RefundTicketEntity> findByUserId(String userId);

    /**
     * 根据订单号查询退票记录
     *
     * @param orderNo 订单号
     * @return 退票记录列表
     */
    List<RefundTicketEntity> findByOrderNo(String orderNo);
}
