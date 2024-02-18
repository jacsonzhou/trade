package com.ybb.trade.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName ums_wallet_unlock_task
 */
@TableName(value ="ums_wallet_unlock_task")
@Data
public class UmsWalletUnlockTask implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private Long memberId;

    /**
     * 
     */
    private BigDecimal amount;

    /**
     * 
     */
    private String coin;

    /**
     * 
     */
    private String orderSn;

    /**
     * 
     */
    private Integer taskStatus;

    /**
     * 
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}