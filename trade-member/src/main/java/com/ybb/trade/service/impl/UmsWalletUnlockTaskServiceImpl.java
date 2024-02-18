package com.ybb.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybb.trade.entity.UmsWalletUnlockTask;
import com.ybb.trade.service.UmsWalletUnlockTaskService;
import com.ybb.trade.mapper.UmsWalletUnlockTaskMapper;
import org.springframework.stereotype.Service;

/**
* @author zhouf
* @description 针对表【ums_wallet_unlock_task】的数据库操作Service实现
* @createDate 2024-02-18 13:47:43
*/
@Service
public class UmsWalletUnlockTaskServiceImpl extends ServiceImpl<UmsWalletUnlockTaskMapper, UmsWalletUnlockTask>
    implements UmsWalletUnlockTaskService{

    @Override
    public UmsWalletUnlockTask selectByOrderSn(String orderSn) {
        return this.baseMapper.selectOne(new QueryWrapper<UmsWalletUnlockTask>().eq("order_sn",orderSn));
    }
}




