package com.ybb.trade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ybb.trade.entity.Wallet;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
* @author zhouf
* @description 针对表【wallet】的数据库操作Mapper
* @createDate 2024-01-28 02:09:42
* @Entity com.ybb.trade.Wallet
*/
public interface WalletMapper extends BaseMapper<Wallet> {
     Wallet getWalletByMemberAndName(@Param("memberId") Long memberId, @Param("name") String name);

     int increaseBalance(@Param("id")Long id,@Param("amount") BigDecimal amount);

     int decreaseBalance(@Param("id")Long id,@Param("amount") BigDecimal amount);

     List<Wallet> listAllByStatus();
}




