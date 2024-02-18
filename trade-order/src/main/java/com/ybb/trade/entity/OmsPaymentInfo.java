package com.ybb.trade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付信息表
 * @TableName oms_payment_info
 */
@TableName(value ="oms_payment_info")
@Data
public class OmsPaymentInfo implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单号（对外业务号）
     */
    private String orderSn;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 支付宝交易流水号
     */
    private String alipayTradeNo;

    /**
     * 支付总金额
     */
    private BigDecimal totalAmount;

    /**
     * 交易内容
     */
    private String subject;

    /**
     * 支付状态
     */
    private String paymentStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 确认时间
     */
    private Date confirmTime;

    /**
     * 回调内容
     */
    private String callbackContent;

    /**
     * 回调时间
     */
    private Date callbackTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}