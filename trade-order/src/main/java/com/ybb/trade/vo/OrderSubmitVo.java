package com.ybb.trade.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Data
public class OrderSubmitVo {
    /** 收获地址的id **/
    @NotNull
    private Long addrId;
    /** 支付方式 **/
    @NotNull
    private String payCoin;
    /** 防重令牌 **/
    @NotNull
    private String orderToken;
    /** 应付价格 **/
    @NotNull
    private BigDecimal payPrice;
    /** 订单备注 **/
    private String remarks;

}
