package cn.bugstack.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 订单状态枚举值对象（用于描述对象属性的值，如枚举，不影响数据库操作的对象，无生命周期）
 * @create 2024-03-16 10:34
 */
@Getter
@AllArgsConstructor
public enum OrderStateVO {

    wait_pay("wait_pay","待支付"),
    completed("completed", "完成"),
    ;

    private final String code;
    private final String desc;

}
