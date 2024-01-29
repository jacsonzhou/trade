package com.ybb.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ybb.trade.common.MessageResult;
import com.ybb.trade.entity.Wallet;

import java.math.BigDecimal;
import java.util.List;

/**
* @author zhouf
* @description 针对表【wallet】的数据库操作Service
* @createDate 2024-01-28 02:09:42
*/
public interface WalletService extends IService<Wallet> {
    void batchSave(List<Wallet> walletList);
    Wallet getByMemberIdAndCurrency(Long memberId,String currency);
    MessageResult recharge(Long memberId, String currencyName, BigDecimal amount);
    int decreaseBalance(Wallet wallet, BigDecimal amount);
    int increaseBalance(Wallet wallet,BigDecimal amount);
    List<Wallet> listAllWallet();
}
