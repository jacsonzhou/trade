package com.ybb.trade.feign;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.entity.AuthMember;
import com.ybb.trade.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

import static com.ybb.trade.constant.SysConstant.SESSION_MEMBER;

@Component
@FeignClient(value = "trade-cart")
public interface CartService {
    @GetMapping(value = "/cart/currentUserCartItems")
    MessageResult getCurrentCartItems();
    @RequestMapping(value = "/cart/clearCartItem")
    MessageResult clearCartItem();
}
