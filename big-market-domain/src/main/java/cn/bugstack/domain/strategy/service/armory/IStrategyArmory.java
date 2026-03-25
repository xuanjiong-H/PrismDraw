package cn.bugstack.domain.strategy.service.armory;

/**
 * @author: L
 * @description:
 * @createTime: 2026-03-24 21:53
 * @version: 1.0
 */
public interface IStrategyArmory {

    /**
     * 装配抽奖策略配置「触发的时机可以为活动审核通过后进行调用」
     *
     * @param strategyId 策略ID
     * @return 装配结果
     */
    boolean assembleLotteryStrategy(Long strategyId);



}
