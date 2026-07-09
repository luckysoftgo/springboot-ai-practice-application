package com.application.ai.practice.agents;


import com.application.ai.practice.model.TaskPlanInfo;
import com.application.ai.practice.model.WorkerResultInfo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 调度 Agent（Orchestrator）
 *
 * 核心职责：
 * 1. 接收用户原始请求
 * 2. 用 AI 分析并拆解为多个子任务
 * 3. 并行分发子任务给对应的 Worker Agent
 * 4. 收集所有 Worker 的执行结果并返回
 *
 * 并发实现：使用 Java 21 虚拟线程（Virtual Thread），轻量高效
 */
@Component
public class OrchestratorAgent {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    private final ExecutorService executor;

    private final TechSupportAgent techSupportAgent;
    private final ProductInfoAgent productInfoAgent;
    private final SalesInfoAgent salesInfoAgent;

    private static final String ORCHESTRATOR_PROMPT = """
            你是一个智能调度中心，负责分析用户请求并拆解为子任务。
            
            你必须严格按以下 JSON 格式输出任务计划（不要输出任何额外文字）：
            {
              "originalRequest": "用户原始请求",
              "subTasks": [
                {
                  "id": 1,
                  "description": "子任务描述",
                  "assignedAgent": "TECH_SUPPORT|PRODUCT|SALES",
                  "keywords": ["关键词1", "关键词2"]
                }
              ]
            }
            
            调度规则：
            - 硬件故障、充电问题、设备损坏、使用问题 → TECH_SUPPORT
            - 软件Bug、App闪退、功能建议、版本问题 → PRODUCT
            - 价格咨询、促销活动、购买意向、订单查询 → SALES
            
            重要：
            - 一个请求可以拆解为多个子任务（如用户同时反映硬件和软件问题）
            - 每个子任务必须明确分配给一种 Agent 类型
            - 只输出 JSON，不要有 markdown 代码块标记
            """;

    public OrchestratorAgent(ChatClient.Builder chatClientBuilder,
                             ObjectMapper objectMapper,
                             TechSupportAgent techSupportAgent,
                             ProductInfoAgent productInfoAgent,
                             SalesInfoAgent salesInfoAgent) {
        this.objectMapper = objectMapper;
        this.techSupportAgent = techSupportAgent;
        this.productInfoAgent = productInfoAgent;
        this.salesInfoAgent = salesInfoAgent;

        // 使用固定线程池（如需虚拟线程，请升级到 JDK 21+ 并改回 newVirtualThreadPerTaskExecutor）
        this.executor = Executors.newFixedThreadPool(10);

        this.chatClient = chatClientBuilder
                .defaultSystem(ORCHESTRATOR_PROMPT)
                .build();
    }

    /**
     * 处理用户请求：拆解任务 + 并行分发 + 收集结果
     *
     * @param userMessage    用户原始消息
     * @param conversationId 会话ID
     * @return 所有 Worker Agent 的执行结果列表
     */
    public List<WorkerResultInfo> process(String userMessage, String conversationId) throws Exception {
        System.out.println("========== 新请求 ==========");
        System.out.println("对话ID: " + conversationId);

        // Step 1: 调度 Agent 分析并拆解任务
        String planJson = chatClient.prompt()
                .user("请分析以下用户请求并拆解为子任务：\n" + userMessage)
                .call()
                .content();

        // 清理可能存在的 markdown 代码块标记
        planJson = cleanJson(planJson);

        TaskPlanInfo plan = objectMapper.readValue(planJson, TaskPlanInfo.class);
        System.out.printf("[调度中心] 任务拆解完成，共 %d 个子任务%n", plan.subTasks().size());

        // Step 2: 并行分发子任务给对应 Worker Agent
        List<Future<WorkerResultInfo>> futures = new ArrayList<>();
        for (TaskPlanInfo.SubTask subTask : plan.subTasks()) {
            futures.add(executor.submit(() -> {
                System.out.printf("[Worker] %s 开始处理子任务 #%d%n",
                        subTask.assignedAgent(), subTask.id());
                long startTime = System.currentTimeMillis();

                String result = dispatchToAgent(subTask, userMessage, conversationId);

                long elapsed = System.currentTimeMillis() - startTime;
                System.out.printf("[Worker] %s 子任务 #%d 完成，耗时 %dms%n",
                        subTask.assignedAgent(), subTask.id(), elapsed);

                boolean requiresEscalation = result.contains("人工") || result.contains("工单");
                return new WorkerResultInfo(subTask.id(), subTask.assignedAgent(), result, requiresEscalation);
            }));
        }

        // Step 3: 收集所有 Worker 结果（最多等待 60 秒）
        List<WorkerResultInfo> results = new ArrayList<>();
        for (Future<WorkerResultInfo> future : futures) {
            results.add(future.get(60, TimeUnit.SECONDS));
        }

        return results;
    }

    /**
     * 根据子任务的 assignedAgent 分发到对应 Worker
     */
    private String dispatchToAgent(TaskPlanInfo.SubTask subTask,
                                   String originalMessage,
                                   String conversationId) {
        // 给 Worker Agent 的消息包含子任务描述和原始请求上下文
        String context = String.format(
                "【你的子任务】%s\n\n【用户原始完整请求，供参考】%s",
                subTask.description(), originalMessage
        );

        // conversationId 加后缀，保证各 Agent 记忆空间隔离，避免串扰
        return switch (subTask.assignedAgent()) {
            case "TECH_SUPPORT" -> techSupportAgent.handle(context, conversationId + "-tech");
            case "PRODUCT"      -> productInfoAgent.handle(context, conversationId + "-prod");
            case "SALES"        -> salesInfoAgent.handle(context, conversationId + "-sales");
            default -> "⚠️ 未知 Agent 类型: " + subTask.assignedAgent();
        };
    }

    /**
     * 清理 AI 输出中可能附带的 markdown 代码块标记
     */
    private String cleanJson(String raw) {
        return raw.replaceAll("(?s)```json\\s*", "")
                  .replaceAll("(?s)```\\s*", "")
                  .trim();
    }
}
