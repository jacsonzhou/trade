package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.service.UmsMemberService;
import com.ybb.trade.service.UmsWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class CoinController {
    @Autowired
    private UmsMemberService umsMemberService;
    @RequestMapping("/coin/list")
    MessageResult listSupportCoin() {
        return MessageResult.success(umsMemberService.listSupportPayCoin());
    }
}
