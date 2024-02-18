package com.ybb.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ybb.trade.entity.UmsMember;
import com.ybb.trade.entity.UmsMemberReceiveAddress;
import com.ybb.trade.vo.LoginVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
* @author zhouf
* @description 针对表【ums_member(会员)】的数据库操作Service
* @createDate 2024-02-08 13:45:31
*/
public interface UmsMemberService extends IService<UmsMember> {
     void register(String phone,String password);
     List<String> listSupportPayCoin();
     LoginVo login(String phone, String password, HttpServletRequest request, HttpServletResponse response);
}
