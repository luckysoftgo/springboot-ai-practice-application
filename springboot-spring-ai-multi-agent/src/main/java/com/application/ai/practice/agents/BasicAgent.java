package com.application.ai.practice.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;

/**
 * Agent 基础抽象类
 *
 * 封装了所有 Agent 共用的：
 * - ChatClient 构建（含系统提示词）
 * - 对话记忆（MessageWindowChatMemory，保留最近20轮）
 * - Advisor 配置
 */
public abstract class BasicAgent {

    protected final ChatClient chatClient;

    protected final ChatMemory chatMemory;

    protected final String agentName;

    protected BasicAgent(ChatClient.Builder chatClientBuilder,
                         String agentName,
                         String systemPrompt) {
        this.agentName = agentName;
        // 使用滑动窗口记忆，保留最近20条消息，避免 context 过长
        this.chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();
        this.chatClient = chatClientBuilder
                .defaultSystem(systemPrompt)
                .build();
    }

    /**
     * 处理用户消息（由子类调用或扩展）
     *
     * @param userMessage    用户消息内容
     * @param conversationId 会话ID（用于隔离不同用户的记忆）
     * @return Agent 的回复内容
     */
    public String handle(String userMessage, String conversationId) {
        return chatClient.prompt()
                .user(userMessage)
                .advisors(spec -> spec
                        // 挂载全局记忆Advisor
                        .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                        // 关键：动态传入当前会话ID
                        .param(ChatMemory.CONVERSATION_ID, conversationId)
                )
                .call()
                .content();
    }

    public String getAgentName() {
        return agentName;
    }
}
