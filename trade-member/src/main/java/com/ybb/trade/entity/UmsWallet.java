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
 * @TableName ums_wallet
 */
@TableName(value ="ums_wallet")
@Data
public class UmsWallet implements Serializable {
    public UmsWallet() {
    }

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Integer memberId;

    /**
     * 
     */
    private BigDecimal balance;

    /**
     * 
     */
    private BigDecimal frozenBalance;

    /**
     * 
     */
    private String coin;

    /**
     * 
     */
    private Integer isLock;

    /**
     * 
     */
    private Integer status;

    /**
     * 
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}