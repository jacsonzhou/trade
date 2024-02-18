package com.ybb.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybb.trade.entity.PmsSkuInfo;
import com.ybb.trade.entity.PmsSpuInfo;
import com.ybb.trade.service.PmsSkuInfoService;
import com.ybb.trade.mapper.PmsSkuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author zhouf
* @description 针对表【pms_sku_info(sku信息)】的数据库操作Service实现
* @createDate 2024-02-06 16:38:04
*/
@Service
public class PmsSkuInfoServiceImpl extends ServiceImpl<PmsSkuInfoMapper, PmsSkuInfo>
    implements PmsSkuInfoService{
    @Autowired
    private PmsSkuInfoService skuInfoService;

    @Override
    public PmsSkuInfo getSkuInfo(Long skuId) {
        return this.baseMapper.selectById(skuId);
    }

}




