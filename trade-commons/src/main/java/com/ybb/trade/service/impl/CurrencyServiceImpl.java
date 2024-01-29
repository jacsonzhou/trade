package com.ybb.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybb.trade.entity.Currency;
import com.ybb.trade.service.CurrencyService;
import com.ybb.trade.mapper.CurrencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author zhouf
* @description 针对表【currency】的数据库操作Service实现
* @createDate 2024-01-28 13:04:00
*/
@Service
public class CurrencyServiceImpl extends ServiceImpl<CurrencyMapper, Currency>
    implements CurrencyService{
    @Autowired
    private CurrencyMapper currencyMapper;
    @Override
    public List<Currency> listAllCurrency() {
        List<Currency> currencyList = this.getBaseMapper().selectList
                (new QueryWrapper<Currency>().eq("status", 1).orderByAsc("sort"));
        return currencyList;
    }

    @Override
    public Currency getCurrencyByNameAndStatus(String name, Integer status) {
        return this.getBaseMapper().selectOne(new QueryWrapper<Currency>().eq("name", name).eq("status", 1));
    }
}




