package com.ybb.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ybb.trade.common.PageUtils;
import com.ybb.trade.entity.WareSkuEntity;
import com.ybb.trade.mq.StockLockedTo;
import com.ybb.trade.vo.OrderMq;
import com.ybb.trade.vo.SkuHasStockVo;
import com.ybb.trade.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 添加库存
     * @param skuId
     * @param wareId
     * @param skuNum
     */
    void addStock(Long skuId, Long wareId, Integer skuNum);

    /**
     * 判断是否有库存
     * @param skuIds
     * @return
     */
    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

    /**
     * 锁定库存
     * @param vo
     * @return
     */
    boolean orderLockStock(WareSkuLockVo vo);


    void unlockStock(StockLockedTo to);

    void unlockStock(OrderMq orderMq);
}

