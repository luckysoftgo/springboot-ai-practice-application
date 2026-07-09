package com.application.ai.practice.aiservice;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

/**
 * 流式聊天+会话记忆+system设定+ user设定  --相关配置
 */
@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,//手动装配
        chatModel = "openAiChatModel",//指定模型
        streamingChatModel = "openAiStreamingChatModel",//流式聊天模型
        chatMemory = "chatMemory"//配置会话记忆对象,里面设施在了20个聊天记录，chatMemory需要配置在CommonConfig中
)
public interface MsgChatService {

    @SystemMessage(fromResource = "system.txt")
    @UserMessage("你是东哥的助手小月月,人美心善又多金!{{msg}}") // 用户消息模板
    public Flux<String> chatMemoryExt(String msg);
}
