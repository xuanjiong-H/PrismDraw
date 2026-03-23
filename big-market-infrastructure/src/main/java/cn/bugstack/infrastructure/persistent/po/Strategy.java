package cn.bugstack.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @author: L
 * @description: 抽奖策略
 * @createTime: 2026-03-23 21:44
 * @version: 1.0
 */

@Data
public class Strategy {
    /*自增ID*/
    private Long id;
    /*抽奖策略ID*/
    private Long strategyId;
    /*抽奖策略描述*/
    private String strategyDesc;
    /*创建时间*/
    private Date createTime;
    /*更新时间*/
    private Date updateTime;
}
