package cn.bugstack.trigger.api.dto;

import lombok.Data;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 抽奖奖品列表，请求对象
 * @create 2024-02-14 09:46
 */
@Data
public class RaffleAwardListRequestDTO {

    // 抽奖策略ID
    private Long strategyId;

}
