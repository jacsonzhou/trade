package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.entity.PmsSkuInfo;
import com.ybb.trade.service.PmsSkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @Autowired
    PmsSkuInfoService skuService;

    /**
     * 获取sku信息
     * @param skuId
     * @return
     */
    @RequestMapping("/skuInfo/{skuId}")
    public MessageResult getSkuInfo(@PathVariable("skuId")Long skuId) {
        return MessageResult.success(skuService.getSkuInfo(skuId));
    }
}
