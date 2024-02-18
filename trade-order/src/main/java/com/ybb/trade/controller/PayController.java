package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.entity.AuthMember;
import com.ybb.trade.service.OmsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.ybb.trade.constant.SysConstant.SESSION_MEMBER;

@RestController
public class PayController {
    @Autowired
    private OmsOrderService orderService;
    @RequestMapping("/pay/{orderSn}")
    public MessageResult payOrder(@PathVariable("orderSn")String orderSn, @RequestParam("password")String password, @SessionAttribute(SESSION_MEMBER) AuthMember member) {
        orderService.payOrder(orderSn,password,member.getId());
        return MessageResult.success("支付成功");
    }
}
