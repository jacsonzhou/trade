package com.ybb.trade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName member
 */
@TableName(value ="member")
@Data
public class Member implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名字
     */
    private String username;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 登录token
     */
    private String accessToken;

    /**
     * 0:普通用户1：认证上家
     */
    private Integer level;

    /**
     * 状态
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}