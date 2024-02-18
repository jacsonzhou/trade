package com.ybb.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybb.trade.entity.PmsSkuInfo;
import com.ybb.trade.entity.PmsSpuInfo;
import com.ybb.trade.service.PmsSkuInfoService;
import com.ybb.trade.service.PmsSpuInfoService;
import com.ybb.trade.mapper.PmsSpuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author zhouf
* @description 针对表【pms_spu_info(spu信息)】的数据库操作Service实现
* @createDate 2024-02-06 16:38:04
*/
@Service
public class PmsSpuInfoServiceImpl extends ServiceImpl<PmsSpuInfoMapper, PmsSpuInfo>
    implements PmsSpuInfoService{
    @Autowired
    private PmsSkuInfoService skuInfoService;
    /**
     * 根据skuId查询spu的信息
     * @param skuId
     * @return
     */
    @Override
    public PmsSpuInfo getSpuInfoBySkuId(Long skuId) {

        PmsSkuInfo skuInfo = skuInfoService.getById(skuId);
        Long spuId = skuInfo.getSpuId();
        PmsSpuInfo pmsSpuInfo = this.baseMapper.selectById(spuId);
        return pmsSpuInfo;
    }
}




