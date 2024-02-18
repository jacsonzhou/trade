package com.ybb.trade.feign;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.vo.SkuInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient(value = "trade-product")
public interface ProductService {
    @RequestMapping("/product/skuInfo/{skuId}")
    MessageResult getSkuInfo(@PathVariable("skuId")Long skuId);
}
