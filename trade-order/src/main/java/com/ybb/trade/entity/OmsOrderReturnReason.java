package com.ybb.trade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 退货原因
 * @TableName oms_order_return_reason
 */
@TableName(value ="oms_order_return_reason")
@Data
public class OmsOrderReturnReason implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 退货原因名
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 启用状态
     */
    private Integer status;

    /**
     * create_time
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}