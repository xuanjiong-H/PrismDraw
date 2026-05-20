package cn.bugstack.domain.rebate.repository;

import cn.bugstack.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import cn.bugstack.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import cn.bugstack.domain.rebate.model.valobj.BehaviorTypeVO;
import cn.bugstack.domain.rebate.model.valobj.DailyBehaviorRebateVO;

import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 行为返利服务仓储接口
 * @create 2024-04-30 14:58
 */
public interface IBehaviorRebateRepository {

    List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO);

    void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates);

    List<BehaviorRebateOrderEntity> queryOrderByOutBusinessNo(String userId, String outBusinessNo);

}
