package com.ybb.trade.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WalletUnlockOrder {
    private String orderSn;
    private Long memberId;
    private BigDecimal amount;
    private String coin;

    @Override
    public String toString() {
        return "WalletUnlockOrder{" +
                "orderSn='" + orderSn + '\'' +
                ", memberId=" + memberId +
                ", amount=" + amount +
                ", coin='" + coin + '\'' +
                '}';
    }
}
