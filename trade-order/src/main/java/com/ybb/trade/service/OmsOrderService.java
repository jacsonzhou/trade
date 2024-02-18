package com.ybb.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ybb.trade.common.PageUtils;
import com.ybb.trade.entity.AuthMember;
import com.ybb.trade.entity.OmsOrder;
import com.ybb.trade.vo.OrderConfirmVo;
import com.ybb.trade.vo.OrderSubmitVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
* @author zhouf
* @description 针对表【oms_order(订单)】的数据库操作Service
* @createDate 2024-02-08 12:40:26
*/
public interface OmsOrderService extends IService<OmsOrder> {
    /**
     * 订单确认页返回需要用的数据
     * @return
     */
    OrderConfirmVo confirmOrder(Long memberId);

    void submitOrder(OrderSubmitVo orderSubmitVo, AuthMember member);

    OmsOrder getOrderByOrderSn(String orderSn);

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageWithItem(Map<String,Object> params);

    void closeOrder(OmsOrder order);

    void payOrder(String orderSn,String password,Long memberId);

}
