package cn.bugstack.infrastructure.persistent.dao;


import cn.bugstack.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: L
 * @description: 策略规则 DAO
 * @createTime: 2026-03-23 21:44
 * @version: 1.0
 */

@Mapper
public interface IStrategyRuleDao {

    List<StrategyRule> queryStrategyRuleList();

}
