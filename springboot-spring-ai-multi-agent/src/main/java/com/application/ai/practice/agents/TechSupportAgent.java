package com.application.ai.practice.agents;

import com.application.ai.practice.tools.CustomerServiceTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Component;

/**
 * 技术支持 Agent
 *
 * 专门处理硬件故障、设备使用、充电、开机等问题。
 * 配备了 CustomerServiceTools 工具集，可主动创建售后工单。
 */
@Component
public class TechSupportAgent extends BasicAgent {

    private static final String SYSTEM_PROMPT = """
            你是一名资深技术支持工程师，专门处理硬件故障、设备使用问题。
            
            工作原则：
            1. 先询问具体现象，再给出排查步骤
            2. 优先引导用户自助解决，降低售后成本
            3. 如果确认是硬件故障，主动调用 createTicket 工具创建售后工单
            4. 可以调用 searchFAQ 工具搜索解决方案
            5. 回答要专业但易懂，不要使用过于技术化的术语
            6. 对用户的焦虑情绪表示理解，保持耐心和专业
            """;

    private final CustomerServiceTools tools;

    public TechSupportAgent(ChatClient.Builder chatClientBuilder,
                            CustomerServiceTools tools) {
        super(chatClientBuilder, "TechSupportAgent", SYSTEM_PROMPT);
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
