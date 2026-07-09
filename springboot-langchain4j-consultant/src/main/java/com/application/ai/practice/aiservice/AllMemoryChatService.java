package com.application.ai.practice.aiservice;

import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

/**
 * 流式聊天+会话记忆 --相关配置
 */
@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,//手动装配
        chatModel = "openAiChatModel",//指定模型
        streamingChatModel = "openAiStreamingChatModel",//流式聊天模型
        chatMemory = "chatMemory"//配置会话记忆对象,里面设施在了20个聊天记录，chatMemory需要配置在CommonConfig中
)
public interface AllMemoryChatService {

    public Flux<String> chatMemory(String message);

}
