package com.ybb.trade.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginVo {
    private long id;
    private String username;
    private String header;
    private String mobile;
    private Long levelId;
    private Integer integration;
    private String socialUid;

    public static LoginVo getLoginInfo(MemberVo member) {
        return LoginVo.builder()
                .id(member.getId())
                .username(member.getUsername())
                .header(member.getHeader())
                .mobile(member.getMobile())
                .levelId(member.getLevelId())
                .integration(member.getIntegration())
                .socialUid(member.getSocialUid())
                .build();

    }
}
