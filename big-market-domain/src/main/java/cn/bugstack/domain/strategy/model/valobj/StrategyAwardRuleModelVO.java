package cn.bugstack.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author: L
 * @description:抽奖策略规则规则值对象；值对象，没有唯一ID，仅限于从数据库查询对象
 * @createTime: 2026-03-26 23:34
 * @version: 1.0
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {

    private String ruleModels;

}
