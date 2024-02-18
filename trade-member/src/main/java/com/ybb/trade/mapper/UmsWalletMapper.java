package com.ybb.trade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ybb.trade.entity.UmsWallet;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
* @author zhouf
* @description 针对表【ums_wallet】的数据库操作Mapper
* @createDate 2024-02-08 13:45:31
* @Entity com.ybb.trade.UmsWallet
*/
public interface UmsWalletMapper extends BaseMapper<UmsWallet> {
    int frozenBalance(@Param("coin") String coin, @Param("memberId") Long memberId, @Param("amount") BigDecimal amount);
    int defrozenBalance(@Param("coin") String coin, @Param("memberId") Long memberId, @Param("amount") BigDecimal amount);
    int releaseBalance(@Param("coin") String coin, @Param("memberId") Long memberId, @Param("amount") BigDecimal amount);
}




