package com.ybb.trade.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import java.util.HashMap;

@Configuration
public class RabbitConfig {
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    /**
     * 账户默认的交换机
     * @return
     */
    @Bean
    public Exchange walletEventExchange() {
        TopicExchange topicExchange = new TopicExchange("wallet-event-exchange", true, false);
        return topicExchange;
    }

    /**
     * 普通队列
     * @return
     */
    @Bean
    public Queue walletUnlockQueue() {
        Queue queue = new Queue("wallet.unlock.queue", true, false, false);
        return queue;
    }


    /**
     * 延迟队列
     * @return
     */
    @Bean
    public Queue walletOrderDelayQueue() {
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "wallet-event-exchange");
        arguments.put("x-dead-letter-routing-key", "wallet.unlock.order");
        arguments.put("x-message-ttl", 120000);
        Queue queue = new Queue("wallet.delay.queue", true, false, false,arguments);
        return queue;
    }


    /**
     * 交换机与普通队列绑定
     * @return
     */
    @Bean
    public Binding walletUnLockedBinding() {
        Binding binding = new Binding("wallet.unlock.queue",
                Binding.DestinationType.QUEUE,
                "wallet-event-exchange",
                "wallet.unlock.order",
                null);
        return binding;
    }


    /**
     * 交换机与延迟队列绑定
     * @return
     */
    @Bean
    public Binding walletDelayQueueBinding() {
        return new Binding("wallet.delay.queue",
                Binding.DestinationType.QUEUE,
                "wallet-event-exchange",
                "wallet.delay.order",
                null);
    }

}
