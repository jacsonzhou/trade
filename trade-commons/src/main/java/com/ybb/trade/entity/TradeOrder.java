package com.ybb.trade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName trade_order
 */
@TableName(value ="trade_order")
@Data
@Builder
public class TradeOrder implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 支付货币名称
     */
    private String currencyName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 购买者id
     */
    private Long buyerId;

    /**
     * 商家id
     */
    private Long sellerId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private BigDecimal amount;

    /**
     *  手续费
     */
    private BigDecimal fee;

    /**
     * 总额
     */
    private BigDecimal totalAmount;

    /**
     * 实际支付
     */
    private BigDecimal payAmount;

    /**
     * 完成时间
     */
    private Date finishTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}