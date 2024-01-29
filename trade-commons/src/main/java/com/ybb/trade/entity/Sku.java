package com.ybb.trade.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName sku
 */
@TableName(value ="sku")
@Data
public class Sku implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * sku名字
     */
    private String skuName;

    /**
     * 图片
     */
    private String skuImg;

    /**
     * 
     */
    private String skuTitle;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 商家
     */
    private Integer memberId;

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