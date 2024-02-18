package com.ybb.trade.feign;

import com.ybb.trade.common.MessageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "trade-product")
public interface ProductService {
    @GetMapping(value = "/product/skuId/{skuId}")
    public MessageResult getSpuInfoBySkuId(@PathVariable("skuId") Long skuId);
}
