package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.service.WalletService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 充值 登录状态直接获取memberId
 */
@RestController
public class WalletController {
    private WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    /**
     *
     * @param memberId
     * @param currency
     * @param amount
     * @return
     */
    @RequestMapping("/recharge/{memberId}/{currency}/{amount}")
    public MessageResult recharge(@PathVariable("memberId")Long memberId,
                                  @PathVariable("currency")String currency,
                                  @PathVariable("amount")BigDecimal amount) {
        return walletService.recharge(memberId,currency,amount);
    }
}
