package com.application.ai.practice.agents;

import com.application.ai.practice.model.CustomerResponse;
import com.application.ai.practice.model.WorkerResultInfo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * 总结 Agent（Summary Agent）
 *
 * 职责：整合所有 Worker Agent 的输出，生成统一、连贯的最终用户回复。
 * 内置降级处理：如果 AI 生成失败，直接拼接 Worker 结果作为兜底。
 */
@Component
public class SummaryInfoAgent {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    private static final String SUMMARY_PROMPT = """
            你是一个专业的客服总结助手，负责整合多个专业团队的处理结果，生成统一的用户回复。
            
            输出要求（严格 JSON 格式，不要有任何额外文字）：
            {
              "reply": "整合后的完整客服回复，语气友好专业，逻辑清晰",
              "actions": ["建议用户执行的具体操作1", "操作2"],
              "humanHandoff": true或false（任一子结果需要人工处理时为true）
            }
            
            整合原则：
            1. 语气统一，避免内容重复
            2. 先解决用户最紧急的问题
            3. actions 列出具体可执行的步骤，不超过5条
            4. 如有工单号，在 reply 中明确告知用户
            """;

    public SummaryInfoAgent(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.chatClient = chatClientBuilder
                .defaultSystem(SUMMARY_PROMPT)
                .build();
    }

    /**
     * 整合 Worker Agent 的输出，生成最终回复
     *
     * @param originalRequest 用户原始请求
     * @param workerResultInfos   所有 Worker Agent 的执行结果
     * @return 整合后的客服响应
     */
    public CustomerResponse summarize(String originalRequest, List<WorkerResultInfo> workerResultInfos) {
        try {
            // 构建上下文：原始请求 + 各 Worker 结果
            StringBuilder context = new StringBuilder();
            context.append("【用户原始请求】\n").append(originalRequest).append("\n\n");
            context.append("【各专业团队处理结果】\n");
            for (WorkerResultInfo result : workerResultInfos) {
                context.append(String.format("[%s团队] %s\n\n",
                        agentTypeLabel(result.agentType()), result.content()));
            }

            String responseJson = chatClient.prompt()
                    .user(context.toString())
                    .call()
                    .content();

            // 清理 markdown 代码块标记
            responseJson = responseJson.replaceAll("(?s)```json\\s*", "")
                                       .replaceAll("(?s)```\\s*", "")
                                       .trim();

            CustomerResponse response = objectMapper.readValue(responseJson, CustomerResponse.class);
            System.out.println("是否转人工: " + response.humanHandoff());
            System.out.println("==========================");
            return response;

        } catch (Exception e) {
            // 降级处理：直接拼接所有 Worker 结果
            System.err.println("[SummaryAgent] 降级处理: " + e.getMessage());
            String fallback = workerResultInfos.stream()
                    .map(WorkerResultInfo::content)
                    .reduce((a, b) -> a + "\n\n" + b)
                    .orElse("感谢您的反馈，我们会尽快为您处理，如有需要请联系人工客服。");
            boolean anyEscalation = workerResultInfos.stream().anyMatch(WorkerResultInfo::requiresEscalation);
            System.out.println("是否转人工(降级): " + anyEscalation);
            System.out.println("==========================");
            return new CustomerResponse(fallback, List.of("联系人工客服"), true);
        }
    }

    private String agentTypeLabel(String agentType) {
        return switch (agentType) {
            case "TECH_SUPPORT" -> "技术支持";
            case "PRODUCT"      -> "产品";
            case "SALES"        -> "销售";
            default -> agentType;
        };
    }
}
