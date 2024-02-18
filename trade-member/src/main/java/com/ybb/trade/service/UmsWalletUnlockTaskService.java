package com.ybb.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ybb.trade.entity.UmsWalletUnlockTask;

/**
* @author zhouf
* @description 针对表【ums_wallet_unlock_task】的数据库操作Service
* @createDate 2024-02-18 13:47:43
*/
public interface UmsWalletUnlockTaskService extends IService<UmsWalletUnlockTask> {

    UmsWalletUnlockTask selectByOrderSn(String orderSn);
}
