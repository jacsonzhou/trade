package com.ybb.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ybb.trade.entity.Currency;

import java.util.List;

/**
* @author zhouf
* @description 针对表【currency】的数据库操作Service
* @createDate 2024-01-28 13:04:00
*/
public interface CurrencyService extends IService<Currency> {
     List<Currency> listAllCurrency();
     Currency getCurrencyByNameAndStatus(String name,Integer status);
}
