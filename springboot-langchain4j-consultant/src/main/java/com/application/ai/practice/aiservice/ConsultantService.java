package com.application.ai.practice.aiservice;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,//手动装配
        chatModel = "openAiChatModel",//指定模型
        streamingChatModel = "openAiStreamingChatModel",
        chatMemoryProvider = "chatMemoryProvider",//配置会话记忆提供者对象
        contentRetriever = "contentRetriever",//配置向量数据库-检索对象 （RAG）
        tools = "reservationTool" //配置工具对象,构建工具对象配置在CommonConfig中（function call）
)
public interface ConsultantService {

    //配置-系统提示词（角色设定）
    @SystemMessage(fromResource = "system.txt")
    Flux<String> chatMemoryExtIsolation(@MemoryId String memoryId, @UserMessage String message);
}



