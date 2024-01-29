package com.ybb.trade.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ybb.trade.common.MessageResult;
import com.ybb.trade.entity.Sku;
import com.ybb.trade.entity.Stock;
import com.ybb.trade.service.SkuService;
import com.ybb.trade.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("admin")
@Slf4j
public class AdminSkuController {
    @Autowired
    private StockService stockService;
    @Autowired
    private SkuService skuService;
    /**
     *
     * @param sellerId 商户id ==>登录后直接缓存获取
     * @param skuId
     * @param amount
     * @return
     */
    @RequestMapping("/sku/add/{sellerId}/{skuId}/{amount}")
    MessageResult updateSku(@PathVariable("sellerId")Long sellerId,@PathVariable("skuId")Long skuId,
                            @PathVariable("amount")Integer amount) {
        Sku sku = skuService.getBaseMapper().selectOne(new QueryWrapper<Sku>().eq("id",skuId).eq("member_id",sellerId));
        if(sku == null) {
            return MessageResult.error("sku 不存在");
        }
        Stock stock = stockService.getBaseMapper().selectOne(new QueryWrapper<Stock>().eq("sku_id",skuId));
        if(stock == null) {
            return MessageResult.error("stock 不存在");
        }
        // 乐观锁 版本号机制
        int result = stockService.increaseStock(stock.getId(),amount,stock.getVersion());
        if(result  <= 0) {
            log.info("更新库存失败");
            return MessageResult.error("更新库存失败");
        }
        return MessageResult.success("更新库存成功");
    }

}
