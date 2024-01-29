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
 * @TableName wallet
 */
@TableName(value ="wallet")
@Data
@Builder
public class Wallet implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 冻结余额 修改成下单--》支付 模式可能有用
     */
    private BigDecimal frozenBalance;

    /**
     * 货币种类： usd cny
     */
    private String currency;

    /**
     * 是否被禁用 0:正常 1禁用
     */
    private Integer isLock;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}