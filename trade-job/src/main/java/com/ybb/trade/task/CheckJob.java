//package com.ybb.trade.task;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.BoundHashOperations;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import com.fasterxml.jackson.core.type.TypeReference;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static com.ybb.trade.constant.WalletConstant.ERROR_THRESHOLD;
//import static com.ybb.trade.constant.WalletConstant.MEMBER_WALLET_SNAPSHOT_PREFIX;
//
///**
// * 凌晨执行
// * 商户余额检测
// * 针对每一种货币 ：当前值 =   redis 缓存 + 当日order总和  阈值
// */
//@Component
//@Slf4j
//public class CheckJob {
//    @Autowired
//    private WalletService walletService;
//    @Autowired
//    private TradeOrderService tradeOrderService;
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    @Scheduled(cron = "0 0 0 * * *")
//    public void checkAllMemberWallet() {
//        // 获取当前数据库数据
//        List<Wallet> allWallets = walletService.listAllWallet();
//        Map<Long, List<Wallet>> memberIdWalletMap = groupWalletsByMemberId(allWallets);
//        LocalDate yesterday = LocalDate.now().minusDays(1);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String yesterdayStr = yesterday.format(formatter);
//
//        memberIdWalletMap.forEach((memberId, walletList) -> {
//            Map<String, BigDecimal> currencyBalanceMap = walletList.stream()
//                    .collect(Collectors.toMap(Wallet::getCurrency, Wallet::getBalance));
//            // redis缓存值
//            Map<String, Object> snapshotValueMap = null;
//            try {
//                snapshotValueMap = getWalletSnapshots(memberId, yesterdayStr);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//            if(snapshotValueMap == null) {
//                log.info("redis 快照数据异常 memberId = {} ,date = {}",memberId,yesterdayStr);
//                return;
//            }
//            checkWalletConsistency(memberId, currencyBalanceMap, snapshotValueMap, yesterdayStr);
//        });
//    }
//
//    private void checkWalletConsistency(Long memberId, Map<String, BigDecimal> currentBalanceMap,
//                                        Map<String, Object> snapshotBalanceMap, String yesterday) {
//        List<TradeOrder> dailyTradeOrders = tradeOrderService.getDailyTradeOrder(yesterday, memberId);
//        Map<String, BigDecimal> totalPayAmountMap = calculateTotalPayAmountByCurrency(dailyTradeOrders);
//
//        for (Map.Entry<String, BigDecimal> entry : totalPayAmountMap.entrySet()) {
//            String currencyName = entry.getKey();
//            BigDecimal totalPayAmount = entry.getValue();
//            BigDecimal snapshotValue = new BigDecimal(String.valueOf(snapshotBalanceMap.get(currencyName)));
//            BigDecimal currentBalance = currentBalanceMap.get(currencyName);
//
//            if (!isDifferenceWithinThreshold(totalPayAmount, snapshotValue, currentBalance)) {
//                log.info("当前商户 memberId = {} ,货币 = {} ,时间 = {} 结算金额异常", memberId, currencyName, yesterday);
//            }
//        }
//    }
//
//    public boolean isDifferenceWithinThreshold(BigDecimal yesterdayPayAmount, BigDecimal snapshotValue, BigDecimal currentBalance) {
//        BigDecimal epsilon = yesterdayPayAmount.add(snapshotValue).subtract(currentBalance).abs();
//        return epsilon.compareTo(BigDecimal.valueOf(ERROR_THRESHOLD)) <= 0;
//    }
//
//    public Map<String, BigDecimal> calculateTotalPayAmountByCurrency(List<TradeOrder> tradeOrders) {
//        return tradeOrders.stream()
//                .collect(Collectors.groupingBy(TradeOrder::getCurrencyName,
//                        Collectors.mapping(TradeOrder::getPayAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
//    }
//
//    public Map<String, Object> getWalletSnapshots(Long memberId, String date) throws JsonProcessingException {
//        String memberIdKey = MEMBER_WALLET_SNAPSHOT_PREFIX + memberId;
//        BoundHashOperations<String, String, Object> hashOps = redisTemplate.boundHashOps(memberIdKey);
//        String str = (String) hashOps.get(date);
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.readValue(str, new TypeReference<Map<String, Object>>() {});
//    }
//
//    private Map<Long, List<Wallet>> groupWalletsByMemberId(List<Wallet> wallets) {
//        return wallets.stream()
//                .collect(Collectors.groupingBy(Wallet::getMemberId));
//    }
//}
