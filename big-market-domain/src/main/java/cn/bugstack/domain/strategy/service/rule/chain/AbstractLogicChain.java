package cn.bugstack.domain.strategy.service.rule.chain;

/**
 * @author: L
 * @description:
 * @createTime: 2026-03-28 00:21
 * @version: 1.0
 */
public abstract class AbstractLogicChain implements ILogicChain {

    private ILogicChain next;

    @Override
    public ILogicChain appendNext(ILogicChain next) {
        this.next = next;
        return next;
    }

    @Override
    public ILogicChain next() {
        return next;
    }

    protected abstract String ruleModel();
}
