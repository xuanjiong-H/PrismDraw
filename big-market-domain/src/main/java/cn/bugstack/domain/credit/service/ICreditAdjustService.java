package cn.bugstack.domain.credit.service;

import cn.bugstack.domain.credit.model.entity.CreditAccountEntity;
import cn.bugstack.domain.credit.model.entity.TradeEntity;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 积分调额接口【正逆向，增减积分】
 * @create 2024-06-01 09:35
 */
public interface ICreditAdjustService {

    /**
     * 创建增加积分额度订单
     * @param tradeEntity 交易实体对象
     * @return 单号
     */
    String createOrder(TradeEntity tradeEntity);

    /**
     * 查询用户积分账户
     * @param userId 用户ID
     * @return 积分账户实体
     */
    CreditAccountEntity queryUserCreditAccount(String userId);

}
