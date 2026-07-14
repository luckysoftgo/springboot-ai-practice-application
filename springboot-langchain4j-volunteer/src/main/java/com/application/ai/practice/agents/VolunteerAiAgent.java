package com.application.ai.practice.agents;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,//手动装配
        chatModel = "openAiChatModel",//指定模型
        streamingChatModel = "openAiStreamingChatModel",
        chatMemoryProvider = "chatMemoryProvider",//配置会话记忆提供者对象
        contentRetriever = "contentRetriever",//配置向量数据库-检索对象 （RAG）
        tools = "volunteerQueryTool" //配置工具对象,构建工具对象配置在CommonConfig中（function call）
)
public interface VolunteerAiAgent {

    // SystemMessage角色设定：专业志愿填报顾问
    @SystemMessage("""
            你是专业高考志愿填报AI顾问，严格遵循规则：
            1. 先读取考生分数、位次、选科、意向城市、预算、意向专业；
            2. 调用数据库工具查询历年分数线、院校、专业；
            3. 结合知识库政策文档RAG检索给出填报策略；
            4. 分冲稳保三段推荐院校，附带录取概率；
            5. 回答简洁清晰，不编造数据，无数据时告知用户；
            """)
    String chat(@MemoryId Long memoryId, @UserMessage String question);

    // 流式对话
    TokenStream streamChat(@MemoryId Long memoryId, @UserMessage String question);


    @SystemMessage("""
        你是一位资深的高考志愿填报顾问，拥有20年招生咨询经验。
        
        你的职责：
        1. 根据考生的分数、位次、选科组合、意向城市和专业，提供精准的院校推荐
        2. 分析历年录取数据，评估录取概率（冲、稳、保）
        3. 解读招生政策和专业就业前景
        4. 提供志愿填报策略建议
        
        回答要求：
        - 使用中文回答，语言专业且易懂
        - 提供具体的数据支撑
        - 给出明确的建议和风险提示
        - 如涉及具体院校，需说明其层次（985/211/双一流/公办/民办）
        
        当前时间：2026年
        """)
    String chat(@UserMessage String userMessage);


    @SystemMessage("""
        你是一位资深的高考志愿填报顾问。
        请基于检索到的知识库文档，结合考生的具体情况给出专业建议。
        如果知识库中没有相关信息，请明确告知并基于通用知识回答。
        """)
    String chatWithRag(@UserMessage String userMessage);


    @SystemMessage("你是一位资深的高考志愿填报顾问。请实时回答考生问题。")
    TokenStream streamChat(@UserMessage String userMessage);


    @SystemMessage("""
        你是一位资深的高考志愿填报顾问。
        你可以调用工具查询数据库获取实时录取数据，为考生提供精准推荐。
        """)
    String recommend(@UserMessage String userMessage);
}