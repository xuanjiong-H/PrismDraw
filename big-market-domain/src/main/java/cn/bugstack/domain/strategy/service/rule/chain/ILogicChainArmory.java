package cn.bugstack.domain.strategy.service.rule.chain;

/**
 * @author: L
 * @description:
 * @createTime: 2026-03-28 01:22
 * @version: 1.0
 */
public interface ILogicChainArmory {

    ILogicChain next();

    ILogicChain appendNext(ILogicChain next);

}
