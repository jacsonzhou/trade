package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.entity.OmsOrder;
import com.ybb.trade.entity.OmsOrderReturnReason;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
public class MqController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @RequestMapping("/sendMessage")
    public MessageResult sendMessage(@RequestParam(value = "num",defaultValue = "10") int num) {
        for (int i = 0; i < num; i++) {
            if(i % 2 == 0) {
                OmsOrderReturnReason reasonEntity = new OmsOrderReturnReason();
                reasonEntity.setId((long) i);
                reasonEntity.setCreateTime(new Date());
                reasonEntity.setName("reason");
                reasonEntity.setStatus(1);
                reasonEntity.setSort(2);
                rabbitTemplate.convertAndSend("exchange.direct","hello2.java",
                        reasonEntity);
            }
//            else {
//                OmsOrder omsOrder = new OmsOrder();
//                omsOrder.setOrderSn(String.valueOf(i + "order_sn"));
//                rabbitTemplate.convertAndSend("exchange.direct","hello2.java",
//                        omsOrder);
//            }
        }
        return MessageResult.success();
    }
}
