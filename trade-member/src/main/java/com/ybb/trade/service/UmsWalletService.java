package com.ybb.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ybb.trade.entity.UmsWallet;

import java.math.BigDecimal;
import java.util.List;

/**
* @author zhouf
* @description 针对表【ums_wallet】的数据库操作Service
* @createDate 2024-02-08 13:45:31
*/
public interface UmsWalletService extends IService<UmsWallet> {
    List<UmsWallet> listWallet(Long memberId);
    UmsWallet queryWallet(String coin,Long memberId);
    int frozenWallet(String coin, Long memberId, BigDecimal amount,String orderSn);
    int deforzenWallet(String coin, Long memberId, BigDecimal amount);
    int releaseWallet(String coin, Long memberId, BigDecimal amount);

}
