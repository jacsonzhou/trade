package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.entity.UmsWallet;
import com.ybb.trade.service.UmsMemberService;
import com.ybb.trade.service.UmsWalletService;
import com.ybb.trade.vo.MemberWalletVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 充值 登录状态直接获取memberId
 */
@RestController
public class WalletController {
    @Autowired
    private UmsWalletService umsWalletService;

    /**
     *
     * @param memberId
     * @return
     */
    @RequestMapping("/wallet/list")
    public List<UmsWallet> listMemberWallet(@RequestParam("memberId")Long memberId) {
        return umsWalletService.listWallet(memberId);
    }
    @RequestMapping("/wallet/{coin}")
    public MessageResult queryWallet(@PathVariable("coin")String coin,@RequestParam("memberId")Long memberId) {
        return MessageResult.success(umsWalletService.queryWallet(coin,memberId));
    }
    @RequestMapping("/lock/wallet")
    public MessageResult lockWallet(@RequestParam("coin")String coin,@RequestParam("memberId")Long memberId,
                                    @RequestParam("amount")BigDecimal amount,
                                    @RequestParam("orderSn")String orderSn) {
       int update = umsWalletService.frozenWallet(coin,memberId,amount,orderSn);
       if(update > 0) {
           return MessageResult.success();
       }else {
           return MessageResult.error("扣除失败");
       }

    }
}
