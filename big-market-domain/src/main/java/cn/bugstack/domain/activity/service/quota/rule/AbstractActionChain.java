package cn.bugstack.domain.activity.service.quota.rule;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 下单规则责任链抽象类
 * @create 2024-03-23 10:16
 */
public abstract class AbstractActionChain implements IActionChain {

    private IActionChain next;

    @Override
    public IActionChain next() {
        return next;
    }

    @Override
    public IActionChain appendNext(IActionChain next) {
        this.next = next;
        return next;
    }

}
