package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import org.springframework.web.bind.annotation.RestController;

/**
 * 购物车
 *
 * Redis中的结构
 * 利用Hash结构存储：
 *
 * key：    “user:[userId]:cart”
 *      field:     [skuId]
 *      value:    CartInfo  (Json)
 * 思路：
 * 1、先检查该用户的购物车里是否已经有该商品
 * 2、如果有商品，只要把对应商品的数量增加上去就可以，同时更新缓存
 * 3、如果没有该商品，则把对应商品插入到购物车中，同时插入缓存。
 */
@RestController
public class CartController {

    public MessageResult addCart(Long skuId)
    {
        return MessageResult.success();
    }
    public MessageResult listCart() {
        return MessageResult.success();
    }
    public MessageResult deleteCart() {
        return MessageResult.success();
    }

}
