package cn.bugstack.domain.strategy.service.rule;

import cn.bugstack.domain.strategy.model.entity.RuleActionEntity;
import cn.bugstack.domain.strategy.model.entity.RuleMatterEntity;

/**
*@author: L
*@description: 抽奖规则过渡接口
*@createTime: 2026-03-26 10:25
*@version: 1.0
*/
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {

    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);
}
