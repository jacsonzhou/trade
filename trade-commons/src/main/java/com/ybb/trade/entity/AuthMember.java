package com.ybb.trade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ybb.trade.vo.MemberVo;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Builder
@Data
public class AuthMember implements Serializable {
    private static final long serialVersionUID = -1L;
    private Long id;
    private Long levelId;
    private String username;
    private String mobile;
    private String header;
    private Integer integration;
    private String socialUid;
    public static AuthMember toAuthMember(MemberVo member) {
        return AuthMember.builder()
                .id(member.getId())
                .header(member.getHeader())
                .integration(member.getIntegration())
                .socialUid(member.getSocialUid())
                .username(member.getUsername())
                .mobile(member.getMobile())
                .levelId(member.getLevelId())
                .build();
    }

}