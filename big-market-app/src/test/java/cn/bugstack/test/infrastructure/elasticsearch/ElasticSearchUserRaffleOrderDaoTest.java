package cn.bugstack.test.infrastructure.elasticsearch;

import cn.bugstack.infrastructure.elasticsearch.IElasticSearchUserRaffleOrderDao;
import cn.bugstack.infrastructure.elasticsearch.po.UserRaffleOrder;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticSearchUserRaffleOrderDaoTest {

    @Resource
    private IElasticSearchUserRaffleOrderDao elasticSearchUserRaffleOrderDao;

    @Test
    public void test_queryUserRaffleOrderList() {
        List<UserRaffleOrder> userRaffleOrders = elasticSearchUserRaffleOrderDao.queryUserRaffleOrderList();
        log.info("测试结果：{}", JSON.toJSONString(userRaffleOrders));
    }

}
