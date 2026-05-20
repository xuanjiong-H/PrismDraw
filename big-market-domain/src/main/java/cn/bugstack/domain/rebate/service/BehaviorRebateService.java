package cn.bugstack.domain.rebate.service;

import cn.bugstack.domain.rebate.event.SendRebateMessageEvent;
import cn.bugstack.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import cn.bugstack.domain.rebate.model.entity.BehaviorEntity;
import cn.bugstack.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import cn.bugstack.domain.rebate.model.entity.TaskEntity;
import cn.bugstack.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import cn.bugstack.domain.rebate.model.valobj.TaskStateVO;
import cn.bugstack.domain.rebate.repository.IBehaviorRebateRepository;
import cn.bugstack.types.common.Constants;
import cn.bugstack.types.event.BaseEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 行为返利服务实现
 * @create 2024-04-30 15:31
 */
@Service
public class BehaviorRebateService implements IBehaviorRebateService {

    @Resource
    private IBehaviorRebateRepository behaviorRebateRepository;
    @Resource
    private SendRebateMessageEvent sendRebateMessageEvent;

    @Override
    public List<String> createOrder(BehaviorEntity behaviorEntity) {
        // 1. 查询返利配置
        List<DailyBehaviorRebateVO> dailyBehaviorRebateVOS = behaviorRebateRepository.queryDailyBehaviorRebateConfig(behaviorEntity.getBehaviorTypeVO());
        if (null == dailyBehaviorRebateVOS || dailyBehaviorRebateVOS.isEmpty()) return new ArrayList<>();

        // 2. 构建聚合对象
        List<String> orderIds = new ArrayList<>();
        List<BehaviorRebateAggregate> behaviorRebateAggregates = new ArrayList<>();
        for (DailyBehaviorRebateVO dailyBehaviorRebateVO : dailyBehaviorRebateVOS) {
            // 拼装业务ID；用户ID_返利类型_外部透彻业务ID
            String bizId = behaviorEntity.getUserId() + Constants.UNDERLINE + dailyBehaviorRebateVO.getRebateType() + Constants.UNDERLINE + behaviorEntity.getOutBusinessNo();
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = BehaviorRebateOrderEntity.builder()
                    .userId(behaviorEntity.getUserId())
                    .orderId(RandomStringUtils.randomNumeric(12))
                    .behaviorType(dailyBehaviorRebateVO.getBehaviorType())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .outBusinessNo(behaviorEntity.getOutBusinessNo())
                    .bizId(bizId)
                    .build();
            orderIds.add(behaviorRebateOrderEntity.getOrderId());

            // MQ 消息对象
            SendRebateMessageEvent.RebateMessage rebateMessage = SendRebateMessageEvent.RebateMessage.builder()
                    .userId(behaviorEntity.getUserId())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .bizId(bizId)
                    .build();

            // 构建事件消息
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> rebateMessageEventMessage = sendRebateMessageEvent.buildEventMessage(rebateMessage);

            // 组装任务对象
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setUserId(behaviorEntity.getUserId());
            taskEntity.setTopic(sendRebateMessageEvent.topic());
            taskEntity.setMessageId(rebateMessageEventMessage.getId());
            taskEntity.setMessage(rebateMessageEventMessage);
            taskEntity.setState(TaskStateVO.create);

            BehaviorRebateAggregate behaviorRebateAggregate = BehaviorRebateAggregate.builder()
                        .userId(behaviorEntity.getUserId())
                        .behaviorRebateOrderEntity(behaviorRebateOrderEntity)
                        .taskEntity(taskEntity)
                        .build();

            behaviorRebateAggregates.add(behaviorRebateAggregate);
        }

        // 3. 存储聚合对象数据
        behaviorRebateRepository.saveUserRebateRecord(behaviorEntity.getUserId(), behaviorRebateAggregates);

        // 4. 返回订单ID集合
        return orderIds;
    }

    @Override
    public List<BehaviorRebateOrderEntity> queryOrderByOutBusinessNo(String userId, String outBusinessNo) {
        return behaviorRebateRepository.queryOrderByOutBusinessNo(userId, outBusinessNo);
    }

}
