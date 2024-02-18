package com.ybb.trade.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * skuId
     */
    private Long skuId;
    /**
     * 是否选中
     */
    private Boolean check = true;
    /**
     * 标题
     */
    private String title;
    /**
     * 图片url
     */
    private String image;

    /**
     * 商品套餐属性
     */
    private List<String> skuAttrValues;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 总价
     */
    private BigDecimal totalPrice;

}
