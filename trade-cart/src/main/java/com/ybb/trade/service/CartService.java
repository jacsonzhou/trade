package com.ybb.trade.service;

import com.ybb.trade.vo.CartItemVo;

import java.util.List;

public interface CartService {
    List<CartItemVo> addCart(Long skuId,Long memberId,int amount);
    List<CartItemVo> deleteCart(Long skuId,Long memberId);
    List<CartItemVo> listCartItem(Long memberId);
    List<CartItemVo> checkItem(Long memberId,Long skuId,int checked);
    List<CartItemVo> updateItemCount(Long memberId,Long skuId,Integer amount);
}
