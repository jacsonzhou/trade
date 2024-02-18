package com.ybb.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ybb.trade.entity.UmsMemberReceiveAddress;

import java.util.List;

/**
* @author zhouf
* @description 针对表【ums_member_receive_address(会员收货地址)】的数据库操作Service
* @createDate 2024-02-08 13:45:31
*/
public interface UmsMemberReceiveAddressService extends IService<UmsMemberReceiveAddress> {
    List<UmsMemberReceiveAddress> listMemberAddress(Long memberId);
}
