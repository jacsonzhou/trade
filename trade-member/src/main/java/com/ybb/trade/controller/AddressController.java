package com.ybb.trade.controller;

import com.ybb.trade.common.MessageResult;
import com.ybb.trade.entity.AuthMember;
import com.ybb.trade.entity.UmsMemberReceiveAddress;
import com.ybb.trade.service.UmsMemberReceiveAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

import static com.ybb.trade.constant.SysConstant.SESSION_MEMBER;

@RestController
@RequestMapping("memberreceiveaddress")
public class AddressController {
    @Autowired
    private UmsMemberReceiveAddressService umsMemberReceiveAddressService;
    @RequestMapping("/address/{memberId}")
    public MessageResult listMemberAddress(@PathVariable("memberId") Long memberId,@SessionAttribute(SESSION_MEMBER) AuthMember member) {
        List<UmsMemberReceiveAddress> addressList = umsMemberReceiveAddressService.listMemberAddress(member.getId());
        return MessageResult.success(addressList);
    }
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public MessageResult info(@PathVariable("id") Long id){
        UmsMemberReceiveAddress memberReceiveAddress = umsMemberReceiveAddressService.getById(id);
        return MessageResult.success(memberReceiveAddress);
    }
}
