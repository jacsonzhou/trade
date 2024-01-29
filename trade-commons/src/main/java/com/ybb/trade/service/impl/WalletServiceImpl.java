package com.ybb.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybb.trade.common.MessageResult;
import com.ybb.trade.entity.Currency;
import com.ybb.trade.entity.Member;
import com.ybb.trade.entity.RechargeOrder;
import com.ybb.trade.entity.Wallet;
import com.ybb.trade.service.CurrencyService;
import com.ybb.trade.service.MemberService;
import com.ybb.trade.service.RechargeOrderService;
import com.ybb.trade.service.WalletService;
import com.ybb.trade.mapper.WalletMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* @author zhouf
* @description 针对表【wallet】的数据库操作Service实现
* @createDate 2024-01-28 02:09:42
*/
@Service
public class WalletServiceImpl extends ServiceImpl<WalletMapper, Wallet>
    implements WalletService{
    @Autowired
    private MemberService memberService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Override
    public void batchSave(List<Wallet> walletList) {
        saveBatch(walletList);
    }

    @Override
    public Wallet getByMemberIdAndCurrency(Long memberId, String currency) {
        return this.getBaseMapper().getWalletByMemberAndName(memberId,currency);
    }

    /**
     * 单jvm幂等  乐观锁
     * @param memberId
     * @param currencyName
     * @param amount
     * @return
     */
    @Override
    @Transactional
    public MessageResult recharge(Long memberId, String currencyName, BigDecimal amount) {
        Member member = memberService.getById(memberId);
        if(member == null) {
            return MessageResult.error("用户异常");
        }
        Currency currency = currencyService.getCurrencyByNameAndStatus(currencyName,1);
        if(currency == null) {
            return MessageResult.error("不支持当前货币充值");
        }
        // 乐观锁 当前账户
        Wallet wallet = this.getByMemberIdAndCurrency(memberId,currencyName);
        if(wallet == null) {
            throw new RuntimeException("账户不存在");
        }
        wallet.setBalance(wallet.getBalance().add(amount));
        int result = this.baseMapper.updateById(wallet);
        if(result == 0) {
            throw new RuntimeException("充值失败");
        }
        // 生成充值记录
        RechargeOrder rechargeOrder = RechargeOrder.builder()
                .amount(amount)
                .memberId(memberId)
                .name(currencyName)
                .status(1)
                .createTime(new Date())
                .build();
        rechargeOrderService.save(rechargeOrder);
        return MessageResult.success();
    }

    @Override
    public int decreaseBalance(Wallet wallet, BigDecimal amount) {
        return this.getBaseMapper().decreaseBalance(wallet.getId(),amount);
    }

    @Override
    public int increaseBalance(Wallet wallet, BigDecimal amount) {
        return this.getBaseMapper().increaseBalance(wallet.getId(),amount);
    }

    @Override
    public List<Wallet> listAllWallet() {
        return this.getBaseMapper().listAllByStatus();
    }
}




