package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.service.CartService;
import com.ybb.trade.vo.CartItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.ExecutionException;


@RestController
public class CartController {
    @Autowired
    private CartService cartService;
    /**
     * 获取当前用户的购物车商品项
     * @return
     */
    @GetMapping(value = "/currentUserCartItems")
    @ResponseBody
    public MessageResult getCurrentCartItems(Long memberId) {

        List<CartItemVo> cartItemVoList = cartService.listCartItem(memberId);
        return MessageResult.success(cartItemVoList);
    }


    @GetMapping(value = "/addCartItem")
    public MessageResult addCartItem(@RequestParam("skuId") Long skuId,
                              @RequestParam("num") Integer num,
                              @RequestParam("memberId")Long memberId){

        List<CartItemVo> list = cartService.addCart(skuId,memberId,num);
        return MessageResult.success(list);
    }
    /**
     * 商品是否选中
     * @param skuId
     * @param checked
     * @param memberId
     * @return
     */
    @GetMapping(value = "/checkItem")
    public MessageResult checkItem(@RequestParam(value = "skuId") Long skuId,
                            @RequestParam(value = "memberId") Long memberId,
                            @RequestParam(value = "checked") Integer checked) {

        List<CartItemVo> list = cartService.checkItem(memberId,skuId,checked);

        return MessageResult.success(list);

    }


    /**
     * 改变商品数量
     * @param skuId
     * @param num
     * @return
     */
    @GetMapping(value = "/countItem")
    public MessageResult countItem(@RequestParam(value = "skuId") Long skuId,
                            @RequestParam(value = "num") Integer num,
                                   @RequestParam(value = "memberId") Long memberId) {

        List<CartItemVo> list = cartService.updateItemCount(memberId,skuId,num);

        return MessageResult.success(list);
    }


    /**
     * 删除商品信息
     * @param skuId
     * @return
     */
    @GetMapping(value = "/deleteItem")
    public MessageResult deleteItem(@RequestParam("skuId") Long skuId,
                             @RequestParam("memberId") Long memberId) {

        List<CartItemVo> list = cartService.deleteCart(skuId,memberId);

        return MessageResult.success(list);

    }

}
