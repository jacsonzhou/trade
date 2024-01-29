package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TradeController {
    @Autowired
    private TradeService tradeService;
    @RequestMapping("/trade")
    public MessageResult trade(Long memberId,String name,Long skuId,
                               Long sellerId, Integer amount) {
        MessageResult messageResult = tradeService.makeTrade(memberId,name,skuId,sellerId,amount);
        return messageResult;
    }
}
