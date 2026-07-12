package cn.bugstack.test;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ZookeeperTest {

    @Resource
    private CuratorFramework curatorFramework;

    @Test
    public void test_all() throws Exception {
        String path = "/big-market-dcc/config/downgradeSwitch";
        String data = "0";
        curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(path, data.getBytes(StandardCharsets.UTF_8));

        for (int i = 0; i < 2; i++) {
            curatorFramework.setData().forPath(path, String.valueOf(i).getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * 创建永久节点
     */
    @Test
    public void createNode() throws Exception {
        String path = "/big-market-dcc/config/downgradeSwitch/test/a";
        String data = "0";
        if (null == curatorFramework.checkExists().forPath(path)) {
            curatorFramework.create().creatingParentsIfNeeded().forPath(path);
        }
    }

    /**
     * 创建临时节点
     */
    @Test
    public void createEphemeralNode() throws Exception {
        String path = "/big-market-dcc/config/epnode";
        String data = "0";
        curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(path, data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 创建临时有序节点
     */
    @Test
    public void crateEphemeralSequentialNode() throws Exception {
        String path = "/big-market-dcc/config/epsnode";
        String data = "0";
        curatorFramework.create()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(path, data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 往节点种设置数据
     */
    @Test
    public void setData() throws Exception {
        curatorFramework.setData().forPath("/big-market-dcc/config/downgradeSwitch", "111".getBytes(StandardCharsets.UTF_8));
        curatorFramework.setData().forPath("/big-market-dcc/config/userWhiteList", "222".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void getData() throws Exception {
        String downgradeSwitch = new String(curatorFramework.getData().forPath("/big-market-dcc/config/downgradeSwitch"), StandardCharsets.UTF_8);
        log.info("测试结果: {}", downgradeSwitch);
        String userWhiteList = new String(curatorFramework.getData().forPath("/big-market-dcc/config/userWhiteList"), StandardCharsets.UTF_8);
        log.info("测试结果: {}", userWhiteList);
    }

    /**
     * 异步修改数据
     */
    @Test
    public void setDataAsync() throws Exception {
        String path = "/big-market-dcc/config/downgradeSwitch";
        String data = "0";
        CuratorListener listener = (client, event) -> {
            Stat stat = event.getStat();
            log.info("stat=" + JSON.toJSONString(stat));
            CuratorEventType eventType = event.getType();
            log.info("eventType=" + eventType.name());
        };
        curatorFramework.getCuratorListenable().addListener(listener);
        curatorFramework.setData().inBackground().forPath(path, data.getBytes(StandardCharsets.UTF_8));
    }


    /**
     * 删除节点
     */
    @Test
    public void deleteData() throws Exception {
        String path = "/big-market-dcc/config/downgradeSwitch";
        curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
    }

    /**
     * 安全删除节点
     */
    @Test
    public void guaranteedDeleteData() throws Exception {
        String path = "/big-market-dcc/config/downgradeSwitch";
        curatorFramework.delete().guaranteed().forPath(path);
    }

    /**
     * 获取子节点下的全部子节点路径集合
     */
    @Test
    public void watchedGetChildren() throws Exception {
        String path = "/big-market-dcc";
        List<String> children = curatorFramework.getChildren().watched().forPath(path);
        log.info("测试结果：{}", JSON.toJSONString(children));
    }


    /**
     * 获取节点数据
     */
    @Test
    public void getDataByPath() throws Exception {
        String path = "/big-market-dcc/config/downgradeSwitch";
        String fullClassName = "";
        String jsonStr = new String(curatorFramework.getData().forPath(path), StandardCharsets.UTF_8);
        Class clazz = Class.forName(fullClassName);
        log.info("测试结果：{}", JSON.parseObject(jsonStr, clazz));
    }
    
}
