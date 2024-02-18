package com.ybb.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybb.trade.entity.UmsMemberReceiveAddress;
import com.ybb.trade.service.UmsMemberReceiveAddressService;
import com.ybb.trade.mapper.UmsMemberReceiveAddressMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author zhouf
* @description 针对表【ums_member_receive_address(会员收货地址)】的数据库操作Service实现
* @createDate 2024-02-08 13:45:31
*/
@Service
public class UmsMemberReceiveAddressServiceImpl extends ServiceImpl<UmsMemberReceiveAddressMapper, UmsMemberReceiveAddress>
    implements UmsMemberReceiveAddressService{

    @Override
    public List<UmsMemberReceiveAddress> listMemberAddress(Long memberId) {
        return this.baseMapper.selectList(new QueryWrapper<UmsMemberReceiveAddress> ().eq("member_id", memberId));
    }
}




