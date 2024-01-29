package com.ybb.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ybb.trade.entity.TradeOrder;

import java.util.List;

/**
* @author zhouf
* @description 针对表【trade_order】的数据库操作Service
* @createDate 2024-01-28 02:09:38
*/
public interface TradeOrderService extends IService<TradeOrder> {
    List<TradeOrder> getDailyTradeOrder(String date,Long memberId);
}
