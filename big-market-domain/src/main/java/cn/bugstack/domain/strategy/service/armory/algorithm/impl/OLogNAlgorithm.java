package cn.bugstack.domain.strategy.service.armory.algorithm.impl;

import cn.bugstack.domain.strategy.model.entity.StrategyAwardEntity;
import cn.bugstack.domain.strategy.service.armory.algorithm.AbstractAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 循环、二分、多线程，查找抽奖算法
 * @create 2024-08-24 10:47
 */
@Slf4j
@Component("oLogNAlgorithm")
public class OLogNAlgorithm extends AbstractAlgorithm {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 预热活动概率值
     * 如概率值为；3、4、2、9，存储为 [1~3]、[4~7]、[8~9]、[10~18]，抽奖时，for循环匹配。
     *
     * @param key                   为策略ID、权重ID
     * @param strategyAwardEntities 对应的奖品概率
     */
    @Override
    public void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyAwardEntities, BigDecimal rateRange) {
        log.info("抽奖算法 OLog(n) 装配 key:{}", key);
        int from = 1;
        int to = 0;

        Map<Map<Integer, Integer>, Integer> table = new HashMap<>();
        for (StrategyAwardEntity strategyAward : strategyAwardEntities) {
            Integer awardId = strategyAward.getAwardId();
            BigDecimal awardRate = strategyAward.getAwardRate();
            to += rateRange.multiply(awardRate).intValue();

            Map<Integer, Integer> ft = new HashMap<>();
            ft.put(from, to);
            table.put(ft, awardId);
            from = to + 1;
        }

        repository.storeStrategyAwardSearchRateTable(key, to, table);
    }

    @Override
    public Integer dispatchAlgorithm(String key) {
        int rateRange = repository.getRateRange(key);
        Map<Map<String, Integer>, Integer> table = repository.getMap(key);
        // 小于等于8 for循环、小于等于16 二分查找、更多检索走多线程
        if (table.size() <= 8) {
            log.info("抽奖算法 OLog(n) 抽奖计算（循环） key:{}", key);
            return forSearch(secureRandom.nextInt(rateRange), table);
        } else if (table.size() <= 16) {
            log.info("抽奖算法 OLog(n) 抽奖计算（二分） key:{}", key);
            return binarySearch(secureRandom.nextInt(rateRange), table);
        } else {
            log.info("抽奖算法 OLog(n) 抽奖计算（多线程） key:{}", key);
            return threadSearch(secureRandom.nextInt(rateRange), table);
        }
    }

    private Integer forSearch(int rateKey, Map<Map<String, Integer>, Integer> table) {
        Integer awardId = null;
        for (Map.Entry<Map<String, Integer>, Integer> entry : table.entrySet()) {
            Map<String, Integer> rangeMap = entry.getKey();

            for (Map.Entry<String, Integer> range : rangeMap.entrySet()) {
                int start = Integer.parseInt(range.getKey());
                int end = range.getValue();

                if (rateKey >= start && rateKey <= end) {
                    awardId = entry.getValue();
                    break;
                }
            }

            if (awardId != null) {
                break;
            }
        }

        return awardId;
    }

    private Integer binarySearch(int rateKey, Map<Map<String, Integer>, Integer> table) {
        List<Map.Entry<Map<String, Integer>, Integer>> entries = new ArrayList<>(table.entrySet());
        entries.sort(Comparator.comparingInt(e -> Integer.parseInt(e.getKey().keySet().iterator().next())));

        int left = 0;
        int right = entries.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Map.Entry<Map<String, Integer>, Integer> entry = entries.get(mid);
            Map<String, Integer> rangeMap = entry.getKey();
            Map.Entry<String, Integer> range = rangeMap.entrySet().iterator().next();

            int start = Integer.parseInt(range.getKey());
            int end = range.getValue();

            if (rateKey < start) {
                right = mid - 1;
            } else if (rateKey > end) {
                left = mid + 1;
            } else {
                return entry.getValue();
            }
        }

        return null;
    }

    private Integer threadSearch(int rateKey, Map<Map<String, Integer>, Integer> table) {
        List<CompletableFuture<Map.Entry<Map<String, Integer>, Integer>>> futures = table.entrySet().stream()
                .map(entry -> CompletableFuture.supplyAsync(() -> {
                    Map<String, Integer> rangeMap = entry.getKey();
                    for (Map.Entry<String, Integer> rangeEntry : rangeMap.entrySet()) {
                        int start = Integer.parseInt(rangeEntry.getKey());
                        int end = rangeEntry.getValue();
                        if (rateKey >= start && rateKey <= end) {
                            return entry;
                        }
                    }
                    return null;
                }, threadPoolExecutor))
                .collect(Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            // 等待所有异步任务完成，同时返回第一个匹配的结果
            allFutures.join();
            for (CompletableFuture<Map.Entry<Map<String, Integer>, Integer>> future : futures) {
                Map.Entry<Map<String, Integer>, Integer> result = future.getNow(null);
                if (result != null) {
                    return result.getValue();
                }
            }
        } catch (CompletionException e) {
            e.printStackTrace();
        }

        return null;
    }

}
