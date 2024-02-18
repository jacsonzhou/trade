package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.entity.WareSkuEntity;
import com.ybb.trade.service.WareSkuService;
import com.ybb.trade.vo.SkuHasStockVo;
import com.ybb.trade.vo.WareSkuLockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 商品库存
 */
@RestController
@RequestMapping("waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;


    /**
     * 锁定库存
     * @param vo
     *
     * 库存解锁的场景
     *      1）、下订单成功，订单过期没有支付被系统自动取消或者被用户手动取消，都要解锁库存
     *      2）、下订单成功，库存锁定成功，接下来的业务调用失败，导致订单回滚。之前锁定的库存就要自动解锁
     *      3）、
     *
     * @return
     */
    @PostMapping(value = "/lock/order")
    public MessageResult orderLockStock(@RequestBody WareSkuLockVo vo) {

        try {
            boolean lockStock = wareSkuService.orderLockStock(vo);
            return MessageResult.success(lockStock);
        } catch (Exception e) {
            return MessageResult.error(e.getMessage());
        }
    }

    /**
     * 查询sku是否有库存
     * @return
     */
    @PostMapping(value = "/hasStock")
    public MessageResult getSkuHasStock(@RequestBody List<Long> skuIds) {

        List<SkuHasStockVo> vos = wareSkuService.getSkuHasStock(skuIds);

        return MessageResult.success(vos);

    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:waresku:info")
    public MessageResult info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return MessageResult.success(wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:waresku:save")
    public MessageResult save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return MessageResult.success();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:waresku:update")
    public MessageResult update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return MessageResult.success();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public MessageResult delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));
        return MessageResult.success();
    }

}
