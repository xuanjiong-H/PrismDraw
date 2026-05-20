package cn.bugstack.domain.award.service.distribute;

import cn.bugstack.domain.award.model.entity.DistributeAwardEntity;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 分发奖品接口
 * @create 2024-05-18 08:22
 */
public interface IDistributeAward {

    void giveOutPrizes(DistributeAwardEntity distributeAwardEntity);

}
