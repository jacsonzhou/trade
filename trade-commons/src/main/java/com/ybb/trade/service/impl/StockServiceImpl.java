package com.ybb.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybb.trade.entity.Stock;
import com.ybb.trade.service.StockService;
import com.ybb.trade.mapper.StockMapper;
import org.springframework.stereotype.Service;

/**
* @author zhouf
* @description 针对表【stock】的数据库操作Service实现
* @createDate 2024-01-28 02:09:34
*/
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock>
    implements StockService{

    @Override
    public int updateStock(int id, int amount, int version) {
        return this.baseMapper.updateStock(id,amount,version);
    }

    @Override
    public int increaseStock(int id, int amount, int version) {
        return this.baseMapper.increaseStock(id,amount,version);
    }
}




