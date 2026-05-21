package cn.bugstack.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 订单交易类型
 * @create 2024-06-08 18:27
 */
@Getter
@AllArgsConstructor
public enum OrderTradeTypeVO {

    credit_pay_trade("credit_pay_trade","积分兑换，需要支付类交易"),
    rebate_no_pay_trade("rebate_no_pay_trade", "返利奖品，不需要支付类交易"),
    ;

    private final String code;
    private final String desc;

}
