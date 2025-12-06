package wiki.chenxun.refund.ticket.ai.agent.service.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wiki.chenxun.refund.ticket.ai.agent.infra.persistence.entity.RefundTicketEntity;
import wiki.chenxun.refund.ticket.ai.agent.infra.persistence.mapper.RefundTicketMapper;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.convert.RefundTicketConvert;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.model.BuyTicket;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.model.RefundTicket;

import java.util.Optional;

/**
 * 退票领域服务
 *
 * @author refund-ticket
 */
@Slf4j
@Service
public class RefundTicketDomainService extends ServiceImpl<RefundTicketMapper, RefundTicketEntity> {


    @Resource
    private BuyTicketDomainService buyTicketDomainService;

    @Resource
    private RefundTicketConvert refundTicketConvert;

    /**
     * 退票
     * @param buyRefundTicketCode
     * @return
     */
    public boolean refundTicket(String buyRefundTicketCode) {
        // 查询购票记录

        Optional<BuyTicket> optionalBuyTicket=  buyTicketDomainService.queryByCode(buyRefundTicketCode);
        if (!optionalBuyTicket.isPresent()) {
            log.error("未找到购票记录");
            return false;
        }
        // 查询退票记录
        LambdaQueryWrapper<RefundTicketEntity> queryWrapper = new LambdaQueryWrapper<RefundTicketEntity>();
        queryWrapper.eq(RefundTicketEntity::getBuyTicketCode, buyRefundTicketCode);
        RefundTicketEntity refundTicketEntity = this.getOne(queryWrapper);
        if (refundTicketEntity != null) {
            log.error("退票记录已存在");
            return false;
        }
        RefundTicket refundTicket = RefundTicket.builder()
                .buyTicketCode(buyRefundTicketCode)
                .code(buyRefundTicketCode)
                .userName(optionalBuyTicket.get().getUserName())
                .status(RefundTicket.RefundStatus.PENDING)
                .build();
        return this.save(refundTicketConvert.toEntity(refundTicket));

    }

}
