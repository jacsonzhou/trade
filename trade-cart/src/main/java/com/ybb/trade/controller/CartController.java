package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.entity.AuthMember;
import com.ybb.trade.service.CartService;
import com.ybb.trade.vo.CartItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.ybb.trade.constant.SysConstant.SESSION_MEMBER;


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
    public MessageResult getCurrentCartItems(@SessionAttribute(SESSION_MEMBER) AuthMember member) {
        List<CartItemVo> cartItemVoList = cartService.listCartItem(member.getId());
        return MessageResult.success(cartItemVoList);
    }


    @GetMapping(value = "/addCartItem")
    @ResponseBody
    public MessageResult addCartItem(@RequestParam("skuId") Long skuId,
                                     @RequestParam("num") Integer num,
                                     @SessionAttribute(SESSION_MEMBER) AuthMember member){
        List<CartItemVo> list = cartService.addCart(skuId,member.getId(),num);
        return MessageResult.success(list);
    }
    /**
     * 商品是否选中
     * @param skuId
     * @param checked
     * @return
     */
    @GetMapping(value = "/checkItem")
    @ResponseBody
    public MessageResult checkItem(@RequestParam(value = "skuId") Long skuId,
                                   @SessionAttribute(SESSION_MEMBER) AuthMember member,
                                   @RequestParam(value = "checked") Integer checked) {
        List<CartItemVo> list = cartService.checkItem(member.getId(),skuId,checked);
        return MessageResult.success(list);

    }


    /**
     * 改变商品数量
     * @param skuId
     * @param num
     * @return
     */
    @GetMapping(value = "/countItem")
    @ResponseBody
    public MessageResult countItem(@RequestParam(value = "skuId") Long skuId,
                            @RequestParam(value = "num") Integer num,
                                   @SessionAttribute(SESSION_MEMBER) AuthMember member) {
        List<CartItemVo> list = cartService.updateItemCount(member.getId(),skuId,num);

        return MessageResult.success(list);
    }


    /**
     * 删除商品信息
     * @param skuId
     * @return
     */
    @GetMapping(value = "/deleteItem")
    @ResponseBody
    public MessageResult deleteItem(@RequestParam("skuId") Long skuId,
                                    @SessionAttribute(SESSION_MEMBER) AuthMember member) {
        List<CartItemVo> list = cartService.deleteCart(skuId, member.getId());
        return MessageResult.success(list);
    }
    @RequestMapping(value = "/clearCartItem")
    @ResponseBody
    public MessageResult clearCartItem(@SessionAttribute(SESSION_MEMBER) AuthMember member) {
        Long memberId = member.getId();
        cartService.clearCartItem(memberId);
        return MessageResult.success();
    }

}
