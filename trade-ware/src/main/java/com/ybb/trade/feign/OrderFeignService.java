package com.ybb.trade.feign;

import com.ybb.trade.common.MessageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("trade-order")
public interface OrderFeignService {

    @GetMapping(value = "/order/status/{orderSn}")
    MessageResult getOrderStatus(@PathVariable("orderSn") String orderSn);

}
