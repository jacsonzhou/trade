package com.ybb.trade.service;

import com.ybb.trade.entity.PmsSkuInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ybb.trade.entity.PmsSpuInfo;

/**
* @author zhouf
* @description 针对表【pms_sku_info(sku信息)】的数据库操作Service
* @createDate 2024-02-06 16:38:04
*/
public interface PmsSkuInfoService extends IService<PmsSkuInfo> {
    PmsSkuInfo getSkuInfo(Long skuId);
}
