package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.service.UmsMemberService;
import com.ybb.trade.vo.LoginVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录
 * 注册
 */
@RestController
public class MemberController {
    @Resource
    public UmsMemberService umsMemberService;
    @RequestMapping("/register")
    public MessageResult registerMember(String phone,String password) {
        try {
            umsMemberService.register(phone,password);
        }catch (Exception e) {
            return MessageResult.error("注册失败");
        }
        return MessageResult.success();
    }
    @RequestMapping("/login")
    public MessageResult login(String phone, String password, HttpServletRequest request, HttpServletResponse response) {
        LoginVo loginVo = umsMemberService.login(phone,password,request,response);
        return MessageResult.success(loginVo);
    }

}
