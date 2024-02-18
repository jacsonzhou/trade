package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.common.PageUtils;
import com.ybb.trade.entity.WareInfoEntity;
import com.ybb.trade.service.WareInfoService;
import com.ybb.trade.vo.FareVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 仓库信息9
 *
 */
@RestController
@RequestMapping("wareinfo")
public class WareInfoController {
    @Autowired
    private WareInfoService wareInfoService;

    /**
     * 获取运费信息
     * @return
     */
    @GetMapping(value = "/fare")
    public MessageResult getFare(@RequestParam("addrId") Long addrId) {

        FareVo fare = wareInfoService.getFare(addrId);

        return MessageResult.success(fare);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public MessageResult list(@RequestParam Map<String, Object> params){
        PageUtils page = wareInfoService.queryPage(params);

        return MessageResult.success(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public MessageResult info(@PathVariable("id") Long id){
		WareInfoEntity wareInfo = wareInfoService.getById(id);

        return MessageResult.success(wareInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:wareinfo:save")
    public MessageResult save(@RequestBody WareInfoEntity wareInfo){
		wareInfoService.save(wareInfo);
        return MessageResult.success();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:wareinfo:update")
    public MessageResult update(@RequestBody WareInfoEntity wareInfo){
		wareInfoService.updateById(wareInfo);

        return MessageResult.success();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:wareinfo:delete")
    public MessageResult delete(@RequestBody Long[] ids){
		wareInfoService.removeByIds(Arrays.asList(ids));

        return MessageResult.success();
    }

}
