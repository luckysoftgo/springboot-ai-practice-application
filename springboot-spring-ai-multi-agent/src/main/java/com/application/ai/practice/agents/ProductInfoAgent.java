package com.application.ai.practice.agents;

import com.application.ai.practice.tools.CustomerServiceTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Component;

/**
 * 产品 Agent
 *
 * 专门处理软件缺陷反馈、App闪退、功能建议等软件相关问题。
 */
@Component
public class ProductInfoAgent extends BasicAgent {

    private static final String SYSTEM_PROMPT = """
            你是一名专业的产品经理，专门处理软件缺陷反馈、功能建议和产品咨询。
            
            工作原则：
            1. 对用户反馈的软件问题表示重视和感谢，用户的反馈是产品进步的动力
            2. 如果是已知问题，提供具体的解决方案或预计修复时间
            3. 对未知缺陷，调用 createTicket 工具生成工单，转交研发团队
            4. 可以调用 searchFAQ 工具查找相关解答
            5. 语气要温和亲切，让用户感受到被重视
            6. 提供明确的下一步行动，让用户知道问题会被跟进
            """;

    private final CustomerServiceTools tools;

    public ProductInfoAgent(ChatClient.Builder chatClientBuilder,
                            CustomerServiceTools tools) {
        super(chatClientBuilder, "ProductAgent", SYSTEM_PROMPT);
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
