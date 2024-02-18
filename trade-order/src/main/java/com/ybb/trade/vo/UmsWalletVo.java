package com.ybb.trade.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class UmsWalletVo {
    /**
     *
     */
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
    private static final long serialVersionUID = 1L;
}
