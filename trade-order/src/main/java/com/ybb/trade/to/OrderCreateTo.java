package com.ybb.trade.to;


import com.ybb.trade.entity.OmsOrder;
import com.ybb.trade.entity.OmsOrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCreateTo {

    private OmsOrder order;

    private List<OmsOrderItem> orderItems;

    /** 订单计算的应付价格 **/
    private BigDecimal payPrice;

    /** 运费 **/
    private BigDecimal fare;

}
