package cn.bugstack.infrastructure.persistent.dao;


import cn.bugstack.infrastructure.persistent.po.Award;
import cn.bugstack.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: L
 * @description: 抽奖策略奖品明细配置 - 概率、规则 DAO
 * @createTime: 2026-03-23 21:44
 * @version: 1.0
 */

@Mapper
public interface IStrategyAwardDao {

    List<StrategyAward> queryStrategyAwardList();

    List<StrategyAward> queryStrategyAwardListByStrategyId(Long strategyId);
}
