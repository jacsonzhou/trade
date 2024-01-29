package com.ybb.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybb.trade.entity.TradeOrder;
import com.ybb.trade.service.TradeOrderService;
import com.ybb.trade.mapper.TradeOrderMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author zhouf
* @description 针对表【trade_order】的数据库操作Service实现
* @createDate 2024-01-28 02:09:38
*/
@Service
public class TradeOrderServiceImpl extends ServiceImpl<TradeOrderMapper, TradeOrder>
    implements TradeOrderService{

    @Override
    public List<TradeOrder> getDailyTradeOrder(String date,Long memberId) {
        return this.baseMapper.getDailyTradeOrder(date,memberId);
    }
}




