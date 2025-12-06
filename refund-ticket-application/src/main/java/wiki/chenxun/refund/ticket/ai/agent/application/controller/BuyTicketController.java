package wiki.chenxun.refund.ticket.ai.agent.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wiki.chenxun.refund.ticket.ai.agent.application.convert.BuyTicketDtoConvert;
import wiki.chenxun.refund.ticket.ai.agent.application.dto.BuyTicketDto;
import wiki.chenxun.refund.ticket.ai.agent.service.domain.service.BuyTicketDomainService;

/**
 * 购票信息控制器
 *
 * @author chenxun
 * @date 2025/12/6
 * @version 1.0
 */
@Tag(name = "购票管理", description = "购票信息查询相关接口")
@Validated
@RestController
@RequestMapping("/buy-ticket")
public class BuyTicketController {

    @Resource
    private  BuyTicketDomainService buyTicketDomainService;

    @Resource
    private BuyTicketDtoConvert buyTicketDtoConvert;

    @Operation(summary = "查询购票信息", description = "根据购票编号查询购票详细信息")
    @GetMapping("/info/{code}")
    public BuyTicketDto info(
            @Parameter(description = "购票编号", required = true, example = "T202512060001")
            @PathVariable("code") String code) {
        return buyTicketDomainService.queryByCode(code)
                .map(buyTicketDtoConvert::toDto).orElse(null);
    }
}
