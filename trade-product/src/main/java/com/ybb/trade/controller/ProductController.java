package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.entity.PmsSkuInfo;
import com.ybb.trade.entity.PmsSpuInfo;
import com.ybb.trade.service.PmsSkuInfoService;
import com.ybb.trade.service.PmsSpuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @Autowired
    PmsSpuInfoService spuService;
    @Autowired
    PmsSkuInfoService skuInfoService;

    /**
     * 获取sku信息
     * @param skuId
     * @return
     */
    @RequestMapping("/skuInfo/{skuId}")
    public MessageResult getSkuInfo(@PathVariable("skuId")Long skuId) {
        return MessageResult.success(skuInfoService.getSkuInfo(skuId));
    }
    /**
     * 根据skuId查询spu的信息
     * @param skuId
     * @return
     */
    @GetMapping(value = "/skuId/{skuId}")
    public MessageResult getSpuInfoBySkuId(@PathVariable("skuId") Long skuId) {

        PmsSpuInfo pmsSpuInfo = spuService.getSpuInfoBySkuId(skuId);

        return MessageResult.success(pmsSpuInfo);
    }

}
