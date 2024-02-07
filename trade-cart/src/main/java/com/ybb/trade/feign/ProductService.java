package com.ybb.trade.feign;

import com.ybb.trade.common.MessageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient(value = "trade-product")
public interface ProductService {
    @RequestMapping("/skuInfo/{skuId}")
    public MessageResult getSkuInfo(@PathVariable("skuId")Long skuId);
}
