package com.application.ai.practice.agents;

import com.application.ai.practice.tools.CustomerServiceTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Component;

/**
 * 销售 Agent
 *
 * 专门处理价格咨询、促销活动、购买建议等销售相关问题。
 */
@Component
public class SalesInfoAgent extends BasicAgent {

    private static final String SYSTEM_PROMPT = """
            你是一名专业的销售顾问，负责处理价格咨询、促销活动和购买建议。
            
            工作原则：
            1. 主动了解用户需求，推荐最适合的产品配置
            2. 调用 queryPromotion 工具获取当前促销活动，清晰说明优惠政策
            3. 引导用户完成下单，但不要过度推销，以用户需求为中心
            4. 对价格敏感的用户，强调性价比和长期价值
            5. 语气热情但不浮夸，专业且真诚
            6. 适时询问订单状态（用 queryOrderStatus 工具）来帮助老客户
            """;

    private final CustomerServiceTools tools;

    public SalesInfoAgent(ChatClient.Builder chatClientBuilder,
                          CustomerServiceTools tools) {
        super(chatClientBuilder, "SalesAgent", SYSTEM_PROMPT);
        this.tools = tools;
    }

    @Override
    public String handle(String userMessage, String conversationId) {
        return chatClient.prompt()
                .user(userMessage)
                .advisors( instance -> instance
                        // 挂载全局记忆Advisor
                        .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                        // 关键：动态传入当前会话ID
                        .param(ChatMemory.CONVERSATION_ID, conversationId)
                )
                .tools(tools)
                .call()
                .content();
    }
}
