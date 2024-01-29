package com.ybb.trade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ybb.trade.entity.TradeOrder;
import com.ybb.trade.entity.Wallet;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author zhouf
* @description 针对表【trade_order】的数据库操作Mapper
* @createDate 2024-01-28 02:09:38
* @Entity com.ybb.trade.TradeOrder
*/
public interface TradeOrderMapper extends BaseMapper<TradeOrder> {
    List<TradeOrder> getDailyTradeOrder(@Param("date") String date, @Param("memberId") Long memberId);
}




