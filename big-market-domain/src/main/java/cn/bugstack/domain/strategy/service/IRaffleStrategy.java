package cn.bugstack.domain.strategy.service;

import cn.bugstack.domain.strategy.model.entity.RaffleAwardEntity;
import cn.bugstack.domain.strategy.model.entity.RaffleFactorEntity;

/**
 * @author: L
 * @description:抽奖策略接口
 * @createTime: 2026-03-26 10:02
 * @version: 1.0
 */
public interface IRaffleStrategy {

    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactoryEntity);
}
