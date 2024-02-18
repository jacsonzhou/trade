package com.ybb.trade.feign;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.vo.WareSkuLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(value = "trade-ware")
public interface WareService {
    @PostMapping(value = "/ware/waresku/hasStock")
    MessageResult getSkuHasStock(@RequestBody List<Long> skuIds);
    /**
     * 获取运费信息
     * @return
     */
    @GetMapping(value = "/ware/wareinfo/fare")
    MessageResult getFare(@RequestParam("addrId") Long addrId);
        @GetMapping(value = "/ware/waresku/lock/order")
    MessageResult orderLockStock(WareSkuLockVo lockVo);
}
