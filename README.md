# PrismDraw

## 项目简介

PrismDraw 是一个基于 DDD 的抽奖营销后端系统，覆盖活动配置、抽奖执行、签到返利、积分兑换、奖品发放、库存回写和异步补偿等完整链路。

## 技术栈

- Java 8
- Spring Boot 2.7.12
- MyBatis 2.1.4
- MySQL 8.0
- Redis 6.2 / Redisson 3.26.0
- RabbitMQ 3.12
- db-router 1.0.4
- Elasticsearch 7.17
- XXL-Job 2.4.1
- Zookeeper

## 核心能力

- 抽奖策略引擎：黑名单、权重、次数解锁、幸运奖兜底
- 活动参与流程：活动装配、抽奖下单、库存扣减、活动账户管理
- 签到返利：日历签到、行为返利、异步消息处理
- 积分体系：积分账户、积分调整、积分兑换商品
- 奖品发放：中奖记录、异步发奖、任务补偿
- 一致性处理：Redis 缓存、MQ、定时任务、延迟回写
- 分库分表：基于 `userId` 路由到 `big_market_01` / `big_market_02`

## 项目结构

```text
PrismDraw
├─ big-market-app            # 应用启动层，Spring Boot 入口与配置
├─ big-market-trigger        # 接口层 / Job / MQ 消费
├─ big-market-domain         # 领域层，核心业务规则
├─ big-market-infrastructure # 基础设施层，DAO / Redis / ES / 仓储实现
├─ big-market-api            # 对外接口定义与 DTO
└─ big-market-types          # 通用类型、枚举、异常、注解
```

## 环境要求

- JDK 8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- RabbitMQ 3.12+
- Zookeeper
- Elasticsearch 7.17+
- XXL-Job Admin
- Docker / Docker Compose

开发环境配置模板位于 `config/` 目录，可按需复制并填写：

- `config/.env.dev.example`
- `config/.env.test.example`
- `config/.env.prod.example`

## 设计特点

- 领域模型前置，核心业务沉到 `big-market-domain`
- 通过责任链处理活动规则校验和库存扣减前置逻辑
- 通过决策树处理抽奖后的后置规则判断
- 通过事件、MQ 和定时任务组合处理奖品发放与库存回写
- 通过 Redis 做高频缓存和库存预扣，降低数据库压力
- 通过分库分表支撑用户维度的数据隔离和扩展
- 通过 `trigger` 作为统一入口，隔离 HTTP、Job 和消息消费
