package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.entity.Member;
import com.ybb.trade.service.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 登录
 * 注册
 */
@RestController
public class MemberController {
    @Resource
    public MemberService memberService;
    @RequestMapping("/register")
    public MessageResult registerMember(String phone,String password) {
        try {
            memberService.register(phone,password);
        }catch (Exception e) {
            return MessageResult.error("注册失败");
        }
        return MessageResult.success();
    }
}
