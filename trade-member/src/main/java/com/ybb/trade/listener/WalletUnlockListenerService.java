package com.ybb.trade.listener;

import com.alibaba.fastjson.JSON;
import com.ybb.trade.common.MessageResult;
import com.ybb.trade.entity.UmsWalletUnlockTask;
import com.ybb.trade.enume.OrderStatusEnum;
import com.ybb.trade.feign.OrderService;
import com.ybb.trade.service.UmsWalletService;
import com.ybb.trade.service.UmsWalletUnlockTaskService;
import com.ybb.trade.vo.OrderMq;
import com.ybb.trade.vo.WalletUnlockOrder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RabbitListener(queues = {"wallet.unlock.queue"})
@Service
public class WalletUnlockListenerService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UmsWalletService umsWalletService;
    @Autowired
    private UmsWalletUnlockTaskService umsWalletUnlockTaskService;
    @RabbitHandler
    public void delayQuene(WalletUnlockOrder walletUnlockOrder, Channel channel, Message message) throws IOException {
        System.out.println("关闭订单 == >" + walletUnlockOrder.toString());
        try {
            unlockWallet(walletUnlockOrder);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e) {
            e.printStackTrace();
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }
    @Transactional
    void unlockWallet(WalletUnlockOrder walletUnlockOrder) {
        String orderSn = walletUnlockOrder.getOrderSn();
        UmsWalletUnlockTask umsWalletUnlockTask = umsWalletUnlockTaskService.selectByOrderSn(orderSn);
        if(umsWalletUnlockTask != null) {
            // 检查订单是否完成
            MessageResult messageResult = orderService.getOrderStatus(orderSn);
            if(messageResult.getCode() == 500) {
                throw new RuntimeException("订单调用异常");
            }
            // order == null,业务整体回滚了；order.status == 0 关单业务积压；order.status==4 已取消：
            OrderMq orderVo = JSON.parseObject(JSON.toJSONString(messageResult.getData()), OrderMq.class);
            if(orderVo == null || orderVo.getStatus() == OrderStatusEnum.CANCLED.getCode()) {
                // 没有处理
                if(umsWalletUnlockTask.getTaskStatus() == 1) {
                    int result = this.umsWalletService.deforzenWallet(walletUnlockOrder.getCoin(),walletUnlockOrder.getMemberId(),walletUnlockOrder.getAmount());
                    if(result == 0) {
                        throw new RuntimeException("回滚异常");
                    }
                    umsWalletUnlockTask.setTaskStatus(2);
                    umsWalletUnlockTaskService.updateById(umsWalletUnlockTask);
                }
            }
        }
    }
}
