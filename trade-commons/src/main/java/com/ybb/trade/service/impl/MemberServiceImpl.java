package com.ybb.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybb.trade.entity.Currency;
import com.ybb.trade.entity.Member;
import com.ybb.trade.entity.Wallet;
import com.ybb.trade.service.CurrencyService;
import com.ybb.trade.service.MemberService;
import com.ybb.trade.mapper.MemberMapper;
import com.ybb.trade.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
* @author zhouf
* @description 针对表【member】的数据库操作Service实现
* @createDate 2024-01-28 02:08:46
*/
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member>
    implements MemberService{
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private WalletService walletService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(String phone, String password) {
        Member member = new Member();
        member.setLevel(0);
        member.setMobile(phone);
        member.setCreateTime(new Date());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(password);
        member.setPassword(encode);
        member.setStatus(1);
        this.baseMapper.insert(member);
        //查询配置的货币种类==>生成货币账户
        List<Currency> list = currencyService.listAllCurrency();
        if(list != null && list.size() > 0) {
            List<Wallet> walletList = new ArrayList<>();
            for(Currency currency : list) {
                String name = currency.getName();
                Wallet wallet = Wallet.builder().memberId(member.getId())
                        .balance(BigDecimal.ZERO).currency(name)
                        .frozenBalance(BigDecimal.ZERO).isLock(0)
                        .status(1).createTime(new Date())
                        .build();
                walletList.add(wallet);
            }
            walletService.batchSave(walletList);
        }
    }

    @Override
    public Member selectByMemberId(Long memberId) {
        return this.baseMapper.selectOne(new QueryWrapper<Member>()
                .eq("id",memberId).eq("status",1));
    }
}




