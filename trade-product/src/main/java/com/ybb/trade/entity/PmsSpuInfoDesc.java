package com.ybb.trade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * spu信息介绍
 * @TableName pms_spu_info_desc
 */
@TableName(value ="pms_spu_info_desc")
@Data
public class PmsSpuInfoDesc implements Serializable {
    /**
     * 商品id
     */
    @TableId
    private Long spuId;

    /**
     * 商品介绍
     */
    private String decript;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}