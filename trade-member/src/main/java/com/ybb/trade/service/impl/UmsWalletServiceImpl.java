package com.ybb.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybb.trade.entity.UmsWallet;
import com.ybb.trade.entity.UmsWalletUnlockTask;
import com.ybb.trade.mapper.UmsWalletUnlockTaskMapper;
import com.ybb.trade.service.UmsWalletService;
import com.ybb.trade.mapper.UmsWalletMapper;
import com.ybb.trade.vo.WalletUnlockOrder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* @author zhouf
* @description 针对表【ums_wallet】的数据库操作Service实现
* @createDate 2024-02-08 13:45:31
*/
@Service
public class UmsWalletServiceImpl extends ServiceImpl<UmsWalletMapper, UmsWallet>
    implements UmsWalletService{
    @Autowired
    private UmsWalletUnlockTaskMapper umsWalletUnlockTaskMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public List<UmsWallet> listWallet(Long memberId) {
        return this.baseMapper.selectList(new QueryWrapper<UmsWallet>().eq("member_id",memberId));
    }

    @Override
    public UmsWallet queryWallet(String coin, Long memberId) {
        return this.baseMapper.selectOne(new QueryWrapper<UmsWallet>().eq("member_id",memberId).eq("coin",coin));
    }

    /**
     * 扣除
     * @param coin
     * @param memberId
     * @param amount
     * @return
     */
    @Override
    @Transactional
    public int frozenWallet(String coin, Long memberId, BigDecimal amount,String orderSn) {
        int updateResult =  this.baseMapper.frozenBalance(coin,memberId,amount);
        if(updateResult == 1) {
            // 任务单
            UmsWalletUnlockTask umsWalletUnlockTask = new UmsWalletUnlockTask();
            umsWalletUnlockTask.setOrderSn(orderSn);
            umsWalletUnlockTask.setCoin(coin);
            umsWalletUnlockTask.setTaskStatus(1);// 未处理
            umsWalletUnlockTask.setMemberId(memberId);
            umsWalletUnlockTask.setCreateTime(new Date());
            umsWalletUnlockTaskMapper.insert(umsWalletUnlockTask);
            // 发送mq
            WalletUnlockOrder walletUnlockOrder = WalletUnlockOrder.builder()
                    .orderSn(orderSn)
                    .amount(amount)
                    .coin(coin)
                    .memberId(memberId)
                    .build();
            rabbitTemplate.convertAndSend("wallet-event-exchange","wallet.delay.order",walletUnlockOrder);
        }
        return updateResult;
    }

    @Override
    public int deforzenWallet(String coin, Long memberId, BigDecimal amount) {
        return this.baseMapper.defrozenBalance(coin,memberId,amount);
    }

    @Override
    public int releaseWallet(String coin, Long memberId, BigDecimal amount) {
        return 0;
    }
}




