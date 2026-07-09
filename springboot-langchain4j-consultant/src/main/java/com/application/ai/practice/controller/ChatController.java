package com.application.ai.practice.controller;

import com.application.ai.practice.aiservice.AllMemoryChatService;
import com.application.ai.practice.aiservice.BaseChatService;
import com.application.ai.practice.aiservice.ConsultantService;
import com.application.ai.practice.aiservice.FluxChatService;
import com.application.ai.practice.aiservice.MsgChatService;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    @Autowired
    private ConsultantService consultantService;

    @Autowired
    private BaseChatService baseChatService;

    @Autowired
    private FluxChatService fluxChatService;

    @Autowired
    private AllMemoryChatService allMemoryChatService;

    @Autowired
    private MsgChatService msgChatService;

    @Autowired
    private OpenAiChatModel openAiChatModel;

    /**
     * [不推荐-直接注入OpenAiChatModel]- 模型聊天 -
     * http://localhost:8080/model/chat
     *
     * @param message
     * @return
     */
    @RequestMapping("/model/chat")
    public String modelChat(@RequestParam(value = "msg", defaultValue = "你是谁？") String message) {
        return openAiChatModel.chat(message);
    }

    /**
     * [使用@AiService手动配置] - 普通聊天
     * http://localhost:8080/ai/service/chat/base
     *
     * @param message
     * @return
     */
    @RequestMapping("/ai/service/chat/base")
    public String aiServiceChatBase(@RequestParam(value = "msg", defaultValue = "你是谁？") String message) {
        return baseChatService.chatBase(message);
    }


    /**
     * [使用@AiService手动配置]- 流式聊天
     * http://localhost:8080/ai/service/chat/flux
     *
     * @param message
     * @return
     */
    @RequestMapping(value = "/ai/service/chat/flux", produces = "text/html;charset=utf-8")
    public Flux<String> aiServiceChatFlux(@RequestParam(value = "msg", defaultValue = "你是谁？") String message) {
        Flux<String> result = fluxChatService.chatFlux(message);
        return result;
    }


    /**
     * [使用@AiService手动配置] 流式聊天+会话记忆
     * http://localhost:8080/ai/service/chat/memory
     *
     * @param message
     * @return
     */
    @RequestMapping(value = "/ai/service/chat/memory", produces = "text/html;charset=utf-8")
    public Flux<String> aiServiceChatMemory(@RequestParam(value = "msg", defaultValue = "你是谁？") String message) {
        return allMemoryChatService.chatMemory(message);
    }

    /**
     * [使用@AiService手动配置] 流式聊天+会话记忆+system设定+ user设定
     * http://localhost:8080/ai/service/chat/memory/ext
     * @param message
     * @return
     */
    @RequestMapping(value = "/ai/service/chat/memory/ext", produces = "text/html;charset=utf-8")
    public Flux<String> aiServiceChatMemoryExt(@RequestParam(value = "msg", defaultValue = "你是谁？") String message) {
        return msgChatService.chatMemoryExt(message);
    }

    /**
     * [使用@AiService手动配置] 流式聊天+会话记忆[隔离]+system设定+ user设定
     * http://localhost:8080/index.html
     * @param message
     * @return
     */
    @RequestMapping(value = "/chat", produces = "text/html;charset=utf-8")
    public Flux<String> chat(@RequestParam("memoryId") String memoryId,
                             @RequestParam("message") String message) {
        return consultantService.chatMemoryExtIsolation(memoryId, message);
    }
}
