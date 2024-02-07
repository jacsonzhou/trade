package com.ybb.trade.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ybb.trade.common.MessageResult;
import com.ybb.trade.feign.ProductService;
import com.ybb.trade.service.CartService;
import com.ybb.trade.vo.CartItemVo;
import com.ybb.trade.vo.SkuInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 购物车
 *
 * Redis中的结构
 * 利用Hash结构存储：
 *
 * key：    “user:[userId]:cart”
 *      field:     [skuId]
 *      value:    CartItemVo  (Json)
 * 思路：
 * 1、先检查该用户的购物车里是否已经有该商品
 * 2、如果有商品，只要把对应商品的数量增加上去就可以，同时更新缓存
 * 3、如果没有该商品，则把对应商品插入到购物车中，同时插入缓存。
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ProductService productService;
    @Override
    public List<CartItemVo> addCart(Long skuId, Long memberId,int amount) {
        // 远程调用product服务
        MessageResult messageResult = productService.getSkuInfo(skuId);
        if(messageResult.getCode() == 500) {
            return listCartItem(memberId);
        }
        BoundHashOperations<String, Object, Object> cartOps = getCartOps(memberId);
        //判断Redis是否有该商品的信息
        String productRedisValue = (String) cartOps.get(skuId);
        if(StringUtils.isEmpty(productRedisValue)) {
            SkuInfoVo skuInfoVo = JSON.parseObject((String) messageResult.getData(), new TypeReference<SkuInfoVo>() {}) ;
            CartItemVo cartItemVo = CartItemVo.builder().check(true)
                    .count(amount)
                    .check(true)
                    .image(skuInfoVo.getSkuDefaultImg())
                    .price(skuInfoVo.getPrice())
                    .totalPrice(skuInfoVo.getPrice().multiply(BigDecimal.valueOf(amount)))
                    .skuId(skuInfoVo.getSkuId())
                    .title(skuInfoVo.getSkuTitle())
                    .build();
            String cartItemJson = JSON.toJSONString(cartItemVo);
            cartOps.put(skuId.toString(), cartItemJson);
        }else {
            //如果有商品，只要把对应商品的数量增加上去就可以，同时更新缓存
            CartItemVo cartItemVo = JSON.parseObject(productRedisValue, CartItemVo.class);
            cartItemVo.setCount(cartItemVo.getCount() + amount);
            //修改redis的数据
            String cartItemJson = JSON.toJSONString(cartItemVo);
            cartOps.put(skuId.toString(),cartItemJson);
        }
        // 返回所有的购物车
        return listCartItem(memberId);
    }

    @Override
    public List<CartItemVo> deleteCart(Long skuId, Long memberId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps(memberId);
        cartOps.delete(String.valueOf(skuId));
        return listCartItem(memberId);
    }

    @Override
    public List<CartItemVo> listCartItem(Long memberId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps(memberId);
        // 获取 key 集合
        Set<Object> keys = cartOps.keys();
        List<CartItemVo> list = new ArrayList<>();
        for (Object skuId : keys) {
            CartItemVo cartItemVo = JSON.parseObject((String) cartOps.get(skuId),new TypeReference<CartItemVo>(){});
            list.add(cartItemVo);
        }
        return list;
    }

    @Override
    public List<CartItemVo> checkItem(Long memberId, Long skuId, int checked) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps(memberId);
        String redisItemValue = (String) cartOps.get(skuId);
        if(StringUtils.isEmpty(redisItemValue)) {
            return listCartItem(memberId);
        }else {
            CartItemVo cartItemVo = JSON.parseObject(redisItemValue,new TypeReference<CartItemVo>(){});
            cartItemVo.setCheck(checked == 1 ? true : false);
            cartOps.put(skuId, JSON.toJSONString(cartItemVo));
        }
        return listCartItem(memberId);
    }

    @Override
    public List<CartItemVo> updateItemCount(Long memberId, Long skuId, Integer amount) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps(memberId);
        String redisItemValue = (String) cartOps.get(skuId);
        if(!StringUtils.isEmpty(redisItemValue)) {
            CartItemVo cartItemVo = JSON.parseObject(redisItemValue,new TypeReference<CartItemVo>(){});
            cartItemVo.setCount(amount);
            cartOps.put(skuId, JSON.toJSONString(cartItemVo));
        }
        return listCartItem(memberId);
    }
    public  BoundHashOperations<String, Object, Object> getCartOps(Long memberId) {
        String cartKey = "user:" + memberId + ":cart";
        BoundHashOperations<String, Object, Object> cartOps = redisTemplate.boundHashOps(cartKey);
        return cartOps;
    }
}
