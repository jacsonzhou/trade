package com.ybb.trade.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *  系统支持的货币
 * @TableName currency
 */
@TableName(value ="currency")
@Data
public class Currency implements Serializable {
    /**
     * 名称
     */
    @TableId
    private String name;

    /**
     * 中文名称
     */
    private String cnName;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 排序 生成账户有用 展示资产有用
     */
    private Integer sort;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}