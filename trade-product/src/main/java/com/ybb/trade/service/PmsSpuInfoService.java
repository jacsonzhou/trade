package com.ybb.trade.service;

import com.ybb.trade.entity.PmsSpuInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zhouf
* @description 针对表【pms_spu_info(spu信息)】的数据库操作Service
* @createDate 2024-02-06 16:38:04
*/
public interface PmsSpuInfoService extends IService<PmsSpuInfo> {
    PmsSpuInfo getSpuInfoBySkuId(Long skuId);
}
