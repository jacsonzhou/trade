package com.ybb.trade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ybb.trade.entity.WareSkuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author zhouf
* @description 针对表【wms_ware_sku(商品库存)】的数据库操作Mapper
* @createDate 2024-02-13 23:25:00
* @Entity com.ybb.trade.entity.WmsWareSku
*/
public interface WmsWareSkuMapper extends BaseMapper<WareSkuEntity> {
    /**
     * 添加入库信息
     * @param skuId
     * @param wareId
     * @param skuNum
     */
    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    Long getSkuStock(@Param("skuId") Long skuId);

    List<Long> listWareIdHasSkuStock(@Param("skuId") Long skuId);

    /**
     * 锁定库存
     * @param skuId
     * @param wareId
     * @param num
     * @return
     */
    Long lockSkuStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("num") Integer num);

    /**
     * 解锁库存
     * @param skuId
     * @param wareId
     * @param num
     */
    void unLockStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("num") Integer num);
}




