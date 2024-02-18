package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.entity.AuthMember;
import com.ybb.trade.service.OmsOrderService;
import com.ybb.trade.vo.OrderConfirmVo;
import com.ybb.trade.vo.OrderSubmitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

import static com.ybb.trade.constant.SysConstant.SESSION_MEMBER;

@RestController
public class SubmitOrderController {
    @Autowired
    private OmsOrderService orderService;
    /**
     * 结算页面 返回信息
     *
     *1、结算api--》订单提交页面
     * 送货地址选择数据
     * 支付方式选择
     * 预计送达时间
     * 商品金额
     * 配送费
     * 优惠券
     * 支付方式 usdt jpy  cny ...
     * 小计
     * 备注啥的
     * @param member
     * @return
     */
    @RequestMapping("/toTrade")
    @ResponseBody
    public MessageResult getSubmitInfo(@SessionAttribute(SESSION_MEMBER) AuthMember member){
        OrderConfirmVo orderConfirmVo = orderService.confirmOrder(member.getId());
        return MessageResult.success(orderConfirmVo);
    }

    /**
     * 下单 选择
     * addressId
     * cart
     * @return
     */
    @RequestMapping("submitOrder")
    public MessageResult submitOrder(OrderSubmitVo orderSubmitVo,@SessionAttribute(SESSION_MEMBER) AuthMember member) {
        try {
            orderService.submitOrder(orderSubmitVo,member);
        }catch (Exception e) {
            e.printStackTrace();
            return MessageResult.error("下单失败：" + e.getMessage());
        }
        return MessageResult.success();
    }
}
