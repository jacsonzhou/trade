package com.ybb.trade.feign;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Component
@FeignClient(value = "trade-member")
public interface MemberService {
    @RequestMapping("/member/memberreceiveaddress/address/{memberId}")
    MessageResult listMemberAddress(@PathVariable("memberId") Long memberId) ;
    @RequestMapping("/member/coin/list")
    MessageResult listPayCoin();
    @RequestMapping("/member/memberreceiveaddress/info/{id}")
    MessageResult info(@PathVariable("id") Long id);
    @RequestMapping("/member/wallet/{coin}")
    MessageResult queryWallet(@PathVariable("coin")String coin,@RequestParam("memberId")Long memberId);
    @RequestMapping("/member/lock/wallet")
    MessageResult lockWallet(@RequestParam("coin")String coin,@RequestParam("memberId")Long memberId,@RequestParam("amount") BigDecimal amount,@RequestParam("orderSn")String orderSn);
}
