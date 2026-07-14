package cn.bugstack.domain.strategy.service.armory.algorithm;

import cn.bugstack.domain.strategy.model.entity.StrategyAwardEntity;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 抽奖算法抽象类
 * @create 2024-08-24 13:04
 */
public abstract class AbstractAlgorithm implements IAlgorithm {

    @Resource
    protected IStrategyRepository repository;

    protected final SecureRandom secureRandom = new SecureRandom();

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum Algorithm {
        O1("o1Algorithm"), OLogN("oLogNAlgorithm");

        private String key;
    }

}
