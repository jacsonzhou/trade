package com.ybb.trade.service;

import com.ybb.trade.common.MessageResult;

public interface TradeService {
    MessageResult makeTrade(Long memberId, String currencyName, Long skuId, Long sellerId, Integer amount);
}
