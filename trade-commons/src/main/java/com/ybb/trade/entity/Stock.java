package com.ybb.trade.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName stock
 */
@TableName(value ="stock")
@Data
public class Stock implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    private Integer skuId;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 锁定库存 下单==》支付有用
     */
    private Integer stockLock;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}