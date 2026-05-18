package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.RaffleActivityOrder;
import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 抽奖活动单Dao
 * @create 2024-03-09 10:08
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IRaffleActivityOrderDao {

    @DBRouter(key = "userId")
    void insert(RaffleActivityOrder raffleActivityOrder);

    @DBRouter
    List<RaffleActivityOrder> queryRaffleActivityOrderByUserId(String userId);

}
