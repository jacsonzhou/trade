package com.ybb.trade.task;

import com.alibaba.fastjson.JSON;
import com.ybb.trade.entity.Wallet;
import com.ybb.trade.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.ybb.trade.constant.WalletConstant.MEMBER_WALLET_SNAPSHOT_PREFIX;

@Component
@Slf4j
public class ShapshotJob {
    @Autowired
    private WalletService walletService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 每日00：00点 账户快照
     *  key              field1     :        value1
     *  member:1001   2024-01-28     "{cny:1000 usd 1000}"
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void walletShapshot() {
        List<Wallet> list = walletService.listAllWallet();
        if (list == null || list.size() == 0) {
            return;
        }
        snapshotWallets(list);
    }

    public void snapshotWallets(List<Wallet> wallets) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        Map<Long, List<Wallet>> memberIdMap = groupByMemberId(wallets);
        for (Map.Entry<Long, List<Wallet>> entry : memberIdMap.entrySet()) {
            Long memberId = entry.getKey();
            List<Wallet> memberWallets = entry.getValue();
            String memberIdKey = MEMBER_WALLET_SNAPSHOT_PREFIX + memberId;
            BoundHashOperations<String, String, Object> hashOps = redisTemplate.boundHashOps(memberIdKey);
            Map<String,Object> result = createSnapshotMap(memberWallets);
            hashOps.put(today, JSON.toJSONString(result));
        }
    }
    private Map<Long, List<Wallet>> groupByMemberId(List<Wallet> wallets) {
        Map<Long, List<Wallet>> memberIdMap = new HashMap<>();
        for (Wallet wallet : wallets) {
            Long memberId = wallet.getMemberId();
            memberIdMap.computeIfAbsent(memberId, k -> new ArrayList<>()).add(wallet);
        }
        return memberIdMap;
    }
    private Map<String, Object> createSnapshotMap(List<Wallet> wallets) {
        Map<String, Object> snapshotMap = new HashMap<>();
        for (Wallet wallet : wallets) {
            snapshotMap.put(wallet.getCurrency(), wallet.getBalance());
        }
        return snapshotMap;
    }
}
