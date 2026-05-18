package cn.bugstack.domain.strategy.service.rule.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: L
 * @description:
 * @createTime: 2026-03-28 00:21
 * @version: 1.0
 */
@Slf4j
public abstract class AbstractLogicChain implements ILogicChain{

    private ILogicChain next;

    @Override
    public ILogicChain next() {
        return next;
    }

    @Override
    public ILogicChain appendNext(ILogicChain next) {
        this.next = next;
        return next;
    }

    protected abstract String ruleModel();

}
