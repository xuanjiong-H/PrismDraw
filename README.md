# Big Market - 抽奖营销系统

一个基于领域驱动设计（DDD）的抽奖营销系统，支持多种抽奖策略、规则过滤和库存管理。

## 项目简介

Big Market 是一个完整的抽奖营销平台，采用 DDD 架构设计，实现了灵活的抽奖策略配置、责任链规则过滤、决策树奖品判定等核心功能。系统支持黑名单过滤、权重抽奖、库存管理、兜底奖品等业务场景。

## 核心功能

- **策略装配**：支持抽奖策略的动态配置和缓存预热
- **责任链过滤**：黑名单检查、权重规则等前置过滤
- **决策树判定**：抽奖次数校验、库存扣减、兜底奖品处理
- **概率计算**：基于概率查找表的高效抽奖算法
- **库存管理**：Redis 缓存库存 + 延迟队列异步更新数据库

## 技术栈

| 技术 | 说明 |
|------|------|
| Spring Boot 2.7.12 | 应用框架 |
| MyBatis | ORM 框架 |
| MySQL 8.0 | 关系型数据库 |
| Redis | 缓存和分布式锁 |
| Redisson | Redis 客户端 |
| Docker | 容器化部署 |
| Maven | 项目构建 |

## 项目架构

```
big-market
├── big-market-app           # 应用启动层 - 配置、启动类
├── big-market-trigger       # 触发器层 - HTTP 接口、定时任务
├── big-market-domain        # 领域层 - 核心业务逻辑
├── big-market-infrastructure # 基础设施层 - 数据库、缓存、外部服务
├── big-market-api           # API 接口定义
└── big-market-types         # 通用类型、枚举、异常
```

### 领域层结构

```
big-market-domain
└── strategy                 # 策略领域
    ├── model                # 领域模型
    │   ├── entity           # 实体
    │   ├── valobj           # 值对象
    │   └── aggregate        # 聚合
    ├── repository           # 仓储接口
    └── service              # 领域服务
        ├── armory           # 策略装配（初始化）
        ├── raffle           # 抽奖执行
        └── rule             # 规则引擎
            ├── chain        # 责任链
            └── tree         # 决策树
```

## 核心设计模式

### 1. 模板方法模式

`AbstractRaffleStrategy` 定义抽奖标准流程：
1. 参数校验
2. 责任链过滤（前置规则）
3. 决策树判定（后置规则）
4. 返回结果

### 2. 责任链模式

处理前置规则过滤：
- `BackListLogicChain` - 黑名单检查
- `RuleWeightLogicChain` - 权重规则
- `DefaultLogicChain` - 默认抽奖

### 3. 决策树模式

处理后置规则判定：
- `RuleLockLogicTreeNode` - 抽奖次数校验
- `RuleStockLogicTreeNode` - 库存扣减
- `RuleLuckAwardLogicTreeNode` - 兜底奖品

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 配置说明

1. 复制配置模板：
```bash
cp big-market-app/src/main/resources/application-dev.yml.example big-market-app/src/main/resources/application-dev.yml
```

2. 修改数据库和 Redis 配置：
```yaml
spring:
  datasource:
    username: your_username
    password: your_password
    url: jdbc:mysql://localhost:3306/big_market

redis:
  sdk:
    config:
      host: localhost
      port: 6379
```

### 构建运行

```bash
# 构建项目
mvn clean package -DskipTests

# 运行应用
java -jar big-market-app/target/big-market-app.jar
```

### Docker 部署

```bash
# 启动基础服务（MySQL、Redis）
docker-compose -f docs/dev-ops/docker-compose-environment.yml up -d

# 启动应用
docker-compose -f docs/dev-ops/docker-compose-app.yml up -d
```

## API 接口

### 策略装配

```
GET /api/v1/raffle/strategy_armory?strategyId=1000001
```

### 查询奖品列表

```
POST /api/v1/raffle/query_raffle_award_list
{
  "strategyId": 1000001
}
```

### 执行抽奖

```
POST /api/v1/raffle/random_raffle
{
  "strategyId": 1000001
}
```

## 项目模块说明

| 模块 | 职责 |
|------|------|
| `big-market-app` | 应用启动配置，包含 Spring Boot 启动类和配置文件 |
| `big-market-trigger` | 触发器层，包含 HTTP 接口和定时任务 |
| `big-market-domain` | 领域层，核心业务逻辑实现 |
| `big-market-infrastructure` | 基础设施层，数据库访问、缓存操作、外部服务调用 |
| `big-market-api` | API 定义层，接口契约和 DTO 定义 |
| `big-market-types` | 通用类型层，枚举、异常、工具类 |

## 设计亮点

1. **领域驱动设计**：清晰的分层架构，业务逻辑与技术实现分离
2. **策略模式**：灵活的抽奖策略配置，支持多种规则组合
3. **责任链模式**：可扩展的规则过滤机制
4. **决策树模式**：复杂的奖品判定逻辑可视化配置
5. **概率查找表**：预计算概率表，抽奖时 O(1) 复杂度
6. **延迟队列**：库存扣减异步更新，保证最终一致性

## License

[Apache License 2.0](LICENSE)
