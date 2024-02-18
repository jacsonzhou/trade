package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.common.PageUtils;
import com.ybb.trade.entity.OmsOrder;
import com.ybb.trade.service.OmsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
public class OrderController {
    @Autowired
    private OmsOrderService orderService;

    /**
     * 根据订单编号查询订单状态
     * @param orderSn
     * @return
     */
    @GetMapping(value = "/status/{orderSn}")
    public MessageResult getOrderStatus(@PathVariable("orderSn") String orderSn) {
        OmsOrder order = orderService.getOrderByOrderSn(orderSn);
        return MessageResult.success(order);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public MessageResult list(Map<String, Object> params){

        PageUtils page = orderService.queryPage(params);

        return MessageResult.success(page);
    }

    /**
     * 分页查询当前登录用户的所有订单信息
     * @param params
     * @return
     */
    @PostMapping("/listWithItem")
    public MessageResult listWithItem(@RequestBody Map<String, Object> params){
        PageUtils page = orderService.queryPageWithItem(params);

        return MessageResult.success(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public MessageResult info(@PathVariable("id") Long id){
		OmsOrder order = orderService.getById(id);

        return MessageResult.success(order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("order:order:save")
    public MessageResult save(@RequestBody OmsOrder order){
		orderService.save(order);

        return MessageResult.success();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public MessageResult update(@RequestBody OmsOrder order){
		orderService.updateById(order);

        return MessageResult.success();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public MessageResult delete(@RequestBody Long[] ids){
		orderService.removeByIds(Arrays.asList(ids));

        return MessageResult.success();
    }

}
