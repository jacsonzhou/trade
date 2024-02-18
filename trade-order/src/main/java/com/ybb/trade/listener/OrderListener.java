package com.ybb.trade.listener;

import com.rabbitmq.client.Channel;
import com.ybb.trade.entity.OmsOrder;
import com.ybb.trade.service.OmsOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RabbitListener(queues = {"order.release.order.queue"})
public class OrderListener {
    @Autowired
    private OmsOrderService orderService;
    @RabbitHandler
    void releaseOrder(Message message, Channel channel, OmsOrder omsOrder) throws IOException {
        log.info("处理消息 ===> {}",omsOrder);
        try {
            orderService.closeOrder(omsOrder);
            // 手动ack
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e) {
            e.printStackTrace();
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }
}
