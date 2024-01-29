package com.ybb.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ybb.trade.entity.Member;

/**
* @author zhouf
* @description 针对表【member】的数据库操作Service
* @createDate 2024-01-28 02:08:46
*/
public interface MemberService extends IService<Member> {
    /**
     * 注册
     * @param phone
     * @param password
     */
    void register(String phone,String password);
    Member selectByMemberId(Long memberId);
}
