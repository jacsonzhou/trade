package com.ybb.trade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ybb.trade.entity.Stock;
import org.apache.ibatis.annotations.Param;

/**
* @author zhouf
* @description 针对表【stock】的数据库操作Mapper
* @createDate 2024-01-28 02:09:34
* @Entity com.ybb.trade.Stock
*/
public interface StockMapper extends BaseMapper<Stock> {
    int updateStock(@Param("id") int id, @Param("amount") int amount, @Param("version") int version);
    int increaseStock(@Param("id") int id, @Param("amount") int amount, @Param("version") int version);
}




