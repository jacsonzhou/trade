package com.ybb.trade.feign;

import com.ybb.trade.common.MessageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "trade-order")
public interface OrderService {
    @GetMapping(value = "/order/status/{orderSn}")
    MessageResult getOrderStatus(@PathVariable("orderSn") String orderSn) ;
}
