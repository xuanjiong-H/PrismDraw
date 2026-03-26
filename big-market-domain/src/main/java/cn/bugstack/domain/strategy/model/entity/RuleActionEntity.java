package cn.bugstack.domain.strategy.model.entity;

import cn.bugstack.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import lombok.*;

/**
 * @author: L
 * @description:规则动作实体
 * @createTime: 2026-03-26 10:28
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity> {

    private String code = RuleLogicCheckTypeVO.ALLOW.getCode();
    private String info = RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;
    private T data;

    static public class RaffleEntity {

    }

    // 抽奖之前
    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static public class RaffleBeforeEntity extends RaffleEntity {
        /**
         * 策略ID
         */
        private Long strategyId;

        /**
         * 权重值Key；用于抽奖时可以选择权重抽奖。
         */
        private String ruleWeightValueKey;

        /**
         * 奖品ID；
         */
        private Integer awardId;
    }


    static public class RaffleCenterEntity extends RaffleEntity {

    }


    static public class RaffleAfterEntity extends RaffleEntity {

    }
}
