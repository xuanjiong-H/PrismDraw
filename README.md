# Big Market - 大营销抽奖系统

基于领域驱动设计（DDD）架构的抽奖营销平台，涵盖后端抽奖引擎与前端互动展示，支持多种抽奖策略、规则引擎、活动运营与积分体系。

## 系统架构总览

```
┌─────────────────────────────────────────────────────┐
│                   big-market-front                   │
│            Next.js 16 + lucky-canvas/react           │
│         大转盘 / 九宫格 / 签到 / 积分兑换              │
└──────────────────────┬──────────────────────────────┘
                       │ HTTP API
┌──────────────────────▼──────────────────────────────┐
│                    big-market                        │
│              Spring Boot 2.7 + DDD                  │
│  ┌─────────┐ ┌──────────┐ ┌──────────────────────┐ │
│  │ trigger │ │ domain   │ │ infrastructure       │ │
│  │ HTTP/MQ │ │ 6大领域   │ │ DB路由/Redis/MQ      │ │
│  └─────────┘ └──────────┘ └──────────────────────┘ │
└──────────────────────┬──────────────────────────────┘
                       │
        ┌──────────────┼──────────────┐
        ▼              ▼              ▼
   MySQL 8.0      Redis 6.2     RabbitMQ
   (分库分表)      (缓存+锁)     (异步消息)
```

## 核心功能

### 后端（big-market）

- **抽奖策略引擎**：责任链前置过滤 + 决策树后置判定，支持黑名单、权重、次数锁、库存扣减、兜底奖品
- **活动运营**：活动装配、SKU 商品管理、账户额度管控（总量/日/月维度）
- **签到返利**：日历签到、行为返利、返利订单创建与 MQ 异步处理
- **积分体系**：积分账户管理、积分充值、积分兑换商品（SKU）
- **奖品发放**：中奖记录、MQ 异步发奖、积分随机发放
- **库存管理**：Redis 缓存库存 + 数据库最终一致（延迟队列异步回写）
- **分库分表**：基于 db-router 中间件，2库4表按 userId 路由

### 前端（big-market-front）

- **大转盘抽奖**：lucky-canvas 大转盘组件
- **九宫格抽奖**：lucky-canvas 九宫格组件
- **日历签到**：签到返利与签到状态查询
- **积分兑换**：SKU 商品展示与积分支付兑换
- **权重规则展示**：策略权重规则可视化
- **会员卡**：用户活动账户额度展示

## 技术栈

### 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.12 | 应用框架 |
| MyBatis | 2.1.4 | ORM 框架 |
| MySQL | 8.0 | 关系型数据库（分库分表） |
| Redis | 6.2 | 缓存、分布式锁 |
| Redisson | 3.26.0 | Redis 客户端 |
| RabbitMQ | 3.12 | 异步消息队列 |
| db-router | 1.0.2 | 自研分库分表路由中间件 |
| Docker | - | 容器化部署 |

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| Next.js | 16.2.1 | React 全栈框架 |
| React | 19.2.4 | UI 框架 |
| lucky-canvas/react | 0.1.13 | 抽奖组件库 |
| Tailwind CSS | 4 | 样式框架 |
| TypeScript | 5 | 类型安全 |

## 后端项目结构

```
big-market
├── big-market-app            # 应用启动层
│   ├── config/               #   Redis、线程池、Guava 配置
│   ├── resources/            #   MyBatis Mapper、多环境配置
│   └── Application.java      #   Spring Boot 启动类
├── big-market-trigger        # 触发器层
│   ├── http/                 #   REST 接口
│   │   ├── RaffleActivityController   活动抽奖/签到/积分/商品
│   │   └── RaffleStrategyController   策略装配/奖品查询/权重
│   ├── job/                  #   定时任务
│   │   ├── SendMessageTaskJob         MQ 消息补偿发送
│   │   ├── UpdateActivitySkuStockJob  活动SKU库存回写
│   │   └── UpdateAwardStockJob        奖品库存回写
│   └── listener/             #   MQ 消息监听
│       ├── SendAwardCustomer          发奖消息消费
│       ├── RebateMessageCustomer      返利消息消费
│       ├── CreditAdjustSuccessCustomer 积分调整成功消费
│       └── ActivitySkuStockZeroCustomer 库存归零消费
├── big-market-domain         # 领域层（核心）
│   ├── strategy/             #   策略领域
│   │   └── service/
│   │       ├── AbstractRaffleStrategy  模板方法定义抽奖流程
│   │       ├── armory/                 策略装配与调度
│   │       ├── raffle/                 抽奖执行
│   │       └── rule/
│   │           ├── chain/              责任链（黑名单→权重→默认）
│   │           └── tree/               决策树（次数锁→库存→兜底）
│   ├── activity/             #   活动领域
│   │   └── service/
│   │       ├── partake/               活动参与（创建抽奖订单）
│   │       ├── quota/                 账户额度（充值/下单责任链）
│   │       ├── product/               SKU 商品服务
│   │       └── armory/                活动装配
│   ├── award/                #   奖品领域（发奖/积分随机发放）
│   ├── rebate/               #   返利领域（签到/行为返利）
│   ├── credit/               #   积分领域（积分账户/交易）
│   └── task/                 #   任务领域（MQ 消息补偿）
├── big-market-infrastructure # 基础设施层
│   ├── persistent/           #   持久化
│   │   ├── dao/              #     21个 DAO 接口
│   │   ├── po/               #     持久化对象
│   │   ├── redis/            #     Redis 服务
│   │   └── repository/       #     仓储实现（6大领域）
│   └── event/                #   事件发布（RabbitMQ）
├── big-market-api            # API 接口定义
│   └── trigger/api/
│       ├── IRaffleActivityService     活动服务接口
│       ├── IRaffleStrategyService     策略服务接口
│       └── dto/                       请求/响应 DTO
└── big-market-types          # 通用类型
    ├── common/               #   常量
    ├── enums/                #   响应码枚举
    ├── event/                #   事件基类
    ├── exception/            #   统一异常
    └── model/                #   统一响应体
```

## 核心设计

### 抽奖流程（模板方法）

`AbstractRaffleStrategy.performRaffle()` 定义标准流程：

```
参数校验 → 责任链过滤(前置) → 决策树判定(后置) → 返回结果
```

### 责任链 - 前置规则过滤

```
BlackListLogicChain → RuleWeightLogicChain → DefaultLogicChain
     黑名单过滤         权重规则抽奖          默认抽奖
```

非默认结果直接返回（如命中黑名单或权重规则），默认结果进入决策树。

### 决策树 - 后置规则判定

```
RuleLockLogicTreeNode → RuleStockLogicTreeNode → RuleLuckAwardLogicTreeNode
     抽奖次数锁            库存扣减               兜底奖品
```

通过决策树引擎动态执行，根据规则配置自动路由。

### 活动下单责任链

活动账户额度充值采用责任链模式：

```
ActivityBaseActionChain → ActivitySkuStockActionChain
   基础额度校验              SKU库存扣减
```

配合交易策略（`ITradePolicy`）支持免支付充值和积分支付两种模式。

### 异步消息与最终一致性

```
领域事件 → MQ消息 → 定时任务补偿
  ↓
RabbitMQ Topic:
  - activity_sku_stock_zero    库存归零通知
  - send_award                 异步发奖
  - send_rebate                异步返利
  - credit_adjust_success      积分调整成功

定时任务（每5秒）:
  - SendMessageTaskJob         扫描失败消息重发
  - UpdateActivitySkuStockJob  Redis库存回写DB
  - UpdateAwardStockJob        奖品库存回写DB
```

### 分库分表

基于 `db-router` 中间件，按 `userId` 路由：

- `big_market`：公共库（策略、规则、奖品配置）
- `big_market_01`：用户库1（活动订单、积分、签到等，4张表）
- `big_market_02`：用户库2（同上）

## API 接口

### 活动服务 `/api/v1/raffle/activity/`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/armory?activityId=` | 活动装配（数据预热） |
| POST | `/draw` | 活动抽奖 |
| POST | `/calendar_sign_rebate` | 日历签到返利 |
| POST | `/is_calendar_sign_rebate` | 查询是否已签到 |
| POST | `/query_user_activity_account` | 查询用户活动账户额度 |
| POST | `/query_user_credit_account` | 查询用户积分余额 |
| POST | `/query_sku_product_list_by_activity_id` | 查询SKU商品列表 |
| POST | `/credit_pay_exchange_sku` | 积分兑换商品 |

### 策略服务 `/api/v1/raffle/strategy/`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/strategy_armory?strategyId=` | 策略装配 |
| POST | `/query_raffle_award_list` | 查询奖品列表（含解锁状态） |
| POST | `/query_raffle_strategy_rule_weight` | 查询权重规则 |
| POST | `/random_raffle` | 随机抽奖 |

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- Node.js 18+
- MySQL 8.0+
- Redis 6.0+

### 1. 启动基础服务

```bash
# 启动 MySQL、Redis（含管理面板）
docker-compose -f docs/dev-ops/docker-compose-environment.yml up -d
```

启动后可访问：
- phpMyAdmin: `http://localhost:8899`
- Redis Commander: `http://localhost:8081`（admin/admin）

### 2. 配置后端

修改 `big-market-app/src/main/resources/application-dev.yml` 中的数据库和 Redis 连接信息。

### 3. 构建运行后端

```bash
# 构建
mvn clean package -DskipTests

# 运行
java -jar big-market-app/target/big-market-app.jar
```

后端服务启动在 `http://localhost:8091`

### 4. 构建运行前端

```bash
cd big-market-front

# 安装依赖
npm install

# 配置后端地址（默认读取环境变量 API_HOST_URL）
# 开发模式启动
npm run dev
```

前端服务启动在 `http://localhost:3000`

### Docker 一键部署

```bash
# 先创建共享网络
docker-compose -f docs/dev-ops/docker-compose-app-common-network.yml up -d

# 启动基础服务
docker-compose -f docs/dev-ops/docker-compose-environment.yml up -d

# 启动应用（后端 + 前端）
docker-compose -f docs/dev-ops/docker-compose-app.yml up -d
```

## 数据库

| 库 | 说明 |
|------|------|
| `big_market` | 公共库：策略、规则树、奖品、活动配置等 |
| `big_market_01` | 用户库1：用户活动订单、积分、签到、中奖记录等 |
| `big_market_02` | 用户库2：同上，按 userId 路由 |

SQL 初始化脚本位于 `docs/dev-ops/mysql/sql/`，Docker 启动时自动加载。

## License

[Apache License 2.0](LICENSE)
