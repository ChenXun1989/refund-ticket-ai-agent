package wiki.chenxun.refund.ticket.ai.agent.service.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import wiki.chenxun.refund.ticket.ai.agent.infra.persistence.entity.BuyTicketEntity;
import wiki.chenxun.refund.ticket.ai.agent.infra.persistence.mapper.BuyTicketMapper;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.convert.BuyTicketConvert;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.model.BuyTicket;

import java.util.Optional;

/**
 * @author: chenxun
 * @date: 2025/12/6
 * @version: 1.0
 * @desc
 **/
@Service
public class BuyTicketDomainService extends ServiceImpl<BuyTicketMapper, BuyTicketEntity> {

    @Resource
    private BuyTicketConvert buyTicketConvert;

    /**
     * 根据code查询购票记录
     * @param code
     * @return
     */
    public Optional<BuyTicket> queryByCode(String code) {
        LambdaQueryWrapper<BuyTicketEntity> queryWrapper = new LambdaQueryWrapper<BuyTicketEntity>();
        queryWrapper.eq(BuyTicketEntity::getCode, code);
        BuyTicketEntity buyTicketEntity = baseMapper.selectOne(queryWrapper);
        return Optional.ofNullable(buyTicketConvert.toDomainModel(buyTicketEntity));
    }
}
