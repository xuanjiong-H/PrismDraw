package cn.bugstack.domain.activity.service.quota.policy;

import cn.bugstack.domain.activity.model.aggregate.CreateQuotaOrderAggregate;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 交易策略接口，包括；返利兑换（不用支付），积分订单（需要支付）
 * @create 2024-06-08 18:04
 */
public interface ITradePolicy {

    void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate);

}
