package com.ybb.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ybb.trade.common.MessageResult;
import com.ybb.trade.entity.*;
import com.ybb.trade.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class TradeServiceImpl implements TradeService {
    @Autowired
    private MemberService memberService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private StockService stockService;
    @Autowired
    private TradeOrderService tradeOrderService;

    /**
     * 幂等
     * @param memberId
     * @param currencyName
     * @param skuId
     * @param sellerId
     * @param amount
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageResult makeTrade(Long memberId,String currencyName,Long skuId, Long sellerId, Integer amount) {
        Member member = memberService.selectByMemberId(memberId);
        if(member == null) {
            return MessageResult.error("用户异常");
        }
        Sku sku = skuService.getBaseMapper().selectOne(new QueryWrapper<Sku>().eq("id",skuId).eq("member_id",sellerId).eq("status",1));
        if(sku  == null ) {
            return MessageResult.error("sku 异常");
        }
        Stock stock = stockService.getBaseMapper().selectOne(new QueryWrapper<Stock>().eq("sku_id",skuId));
        if(stock == null) {
            return MessageResult.error("stock 异常");
        }
        if(stock.getStock()  < amount) {
            return MessageResult.error("stock 不足");
        }
        Wallet buyer = walletService.getByMemberIdAndCurrency(memberId,currencyName);
        Wallet seller = walletService.getByMemberIdAndCurrency(sellerId,currencyName);
        if(buyer == null) {
            throw new RuntimeException("买家账户异常");
        }
        if(seller == null) {
            throw new RuntimeException("商户账户异常");
        }
        BigDecimal totalValue = sku.getPrice().multiply(BigDecimal.valueOf(amount)).setScale(2,BigDecimal.ROUND_DOWN);
        if(buyer.getBalance().compareTo(totalValue) < 0) {
            return MessageResult.error("账户金额不足");
        }
        // 资金转移
        transfer(buyer,seller,totalValue);
        // 扣减库存
        decreaseStock(stock,amount);
        // 生成订单
        tradeOrder(currencyName,sellerId,memberId,skuId,sku.getPrice(),amount,totalValue);
        return MessageResult.success();
    }

    private void tradeOrder(String currencyName,Long sellerId, Long memberId, Long skuId,
                            BigDecimal price, Integer amount,BigDecimal total) {
        TradeOrder tradeOrder = TradeOrder.builder()
                .amount(BigDecimal.valueOf(amount))
                .price(price)
                .currencyName(currencyName)
                .payAmount(total)
                .buyerId(memberId)
                .sellerId(sellerId)
                .totalAmount(BigDecimal.ZERO)
                .fee(BigDecimal.ZERO)
                .finishTime(new Date())
                .createTime(new Date())
                .status(1)
                .build();
        tradeOrderService.save(tradeOrder);
    }

    private void decreaseStock(Stock stock, Integer amount) {
        int result  = stockService.updateStock(stock.getId(),amount,stock.getVersion());
        if(result <= 0) {
            throw new RuntimeException("库存更新失败");
        }
    }

    void transfer(Wallet buyer,Wallet seller,BigDecimal transferValue) {
        int result = 0;
        result = walletService.increaseBalance(seller,transferValue);
        if(result <= 0 ){
            throw  new RuntimeException("更新失败");
        }
        result = walletService.decreaseBalance(buyer,transferValue);
        if(result  <= 0) {
            throw  new RuntimeException("更新失败");
        }
    }
}
