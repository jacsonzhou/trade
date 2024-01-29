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
 * @TableName recharge_order
 */
@TableName(value ="recharge_order")
@Data
@Builder
public class RechargeOrder implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 编号
     */
    private String orderSn;

    /**
     * 
     */
    private Long memberId;

    /**
     * 货币
     */
    private String name;

    /**
     * 数量
     */
    private BigDecimal amount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 状态
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}