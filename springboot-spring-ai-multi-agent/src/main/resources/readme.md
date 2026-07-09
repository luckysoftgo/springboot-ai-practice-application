# Spring AI 智能客服系统

基于 **Spring AI 2.0.0 + Spring Boot 4.0.0 + Java 21 ** 实现的 Multi-Agent 多智能体系统，配套 React + Vite 前端。

## 架构说明

采用 **Orchestrator-Workers** 模式：

```
用户请求
   ↓
[OrchestratorAgent] 分析并拆解为子任务
   ├─ 子任务1: 硬件问题 → [TechSupportAgent]
   └─ 子任务2: 软件问题 → [ProductInfoAgent]
           ↘ 并行执行 ↙
   [SummaryAgent] 整合所有结果
   ↓
最终回复给用户
```

## 运行前提

- JDK 21+
- Maven 3.8+
- 一个兼容 OpenAI 格式的 API Key（支持 OpenAI / DeepSeek / 通义千问 等）

## 快速验证

```bash
# 单一问题（软件Bug）
curl -X POST http://localhost:8080/api/customer-service/chat \
  -H "Content-Type: application/json" \
  -d "{\"message\":\"你们的App总是闪退，怎么解决？\"}"

# 复合问题（同时触发多个 Agent）
curl -X POST http://localhost:8080/api/customer-service/chat \
  -H "Content-Type: application/json" \
  -d "{\"message\":\"我买的手机充不进电，而且你们的App总是闪退，你们这是什么质量？\"}"

# 销售咨询
curl -X POST http://localhost:8080/api/customer-service/chat \
  -H "Content-Type: application/json" \
  -d "{\"message\":\"最新款手机现在有什么优惠活动？\"}"

# 健康检查
curl http://localhost:8080/api/customer-service/health
```

## 响应示例

```json
{
  "reply": "您好！非常抱歉给您带来不便...",
  "actions": [
    "检查充电口是否有异物",
    "升级App到v3.2.1版本",
    "若问题持续，我们已为您创建工单 TKT-xxxxxxxx"
  ],
  "humanHandoff": true
}
```

## 项目结构

```
├── pom.xml
└── src/main/
   ├── java/com/mszlu/ai/agent/
   │   ├── MultiAgentApplication.java     # 启动类
   │   ├── agents/
   │   │   ├── BasicAgent.java            # Agent 基础抽象类
   │   │   ├── OrchestratorAgent.java     # 调度 Agent（核心）
   │   │   ├── TechSupportAgent.java      # 技术支持 Worker
   │   │   ├── ProductInfoAgent.java      # 产品 Worker
   │   │   ├── SalesInfoAgent.java        # 销售 Worker
   │   │   └── SummaryInfoAgent.java      # 总结 Agent
   │   ├── controller/
   │   │   └── CustomerInfoController.java
   │   ├── model/
   │   │   ├── TaskPlanInfo.java
   │   │   ├── WorkerResult.java
   │   │   ├── CustomerResponse.java
   │   │   └── OrderStatus.java
   │   ├── service/
   │   │   └── CustomerService.java
   │   └── tools/
   │       └── CustomerServiceTools.java  # @Tool 工具集
   └── resources/
       └── application.yml

```

## 关键技术点

| 技术 | 说明                               |
|------|----------------------------------|
| `@Tool` 注解 | 将 Java 方法暴露给 AI 调用，零配置           |
| `MessageChatMemoryAdvisor` | 自动管理多轮对话记忆                       |
| `Executors.newFixedThreadPool(10)` | Java 线程池并行分发 Worker Agent        |
| `conversationId` 隔离 | 每个 Worker Agent 使用独立后缀，避免记忆串扰    |
| 降级处理 | SummaryInfoAgent 失败时自动降级拼接，保证可用性 |

