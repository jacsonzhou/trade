package com.ybb.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ybb.trade.entity.Stock;

/**
* @author zhouf
* @description 针对表【stock】的数据库操作Service
* @createDate 2024-01-28 02:09:34
*/
public interface StockService extends IService<Stock> {
    int updateStock(int id,int amount,int version);

    int increaseStock(int id,int amount ,int version);

}
