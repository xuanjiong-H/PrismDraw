package cn.bugstack.trigger.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 用户活动账户请求对象
 * @create 2024-05-03 07:17
 */
@Data
public class UserActivityAccountRequestDTO implements Serializable {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

}
