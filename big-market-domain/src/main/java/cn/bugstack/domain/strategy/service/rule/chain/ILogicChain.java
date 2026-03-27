package cn.bugstack.domain.strategy.service.rule.chain;

/**
 * @author: L
 * @description:责任链过滤
 * @createTime: 2026-03-28 00:12
 * @version: 1.0
 */
public interface ILogicChain extends ILogicChainArmory{

    /**
     * 责任链接口
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @return 奖品ID
     */
    Integer logic(String userId, Long strategyId);

}
