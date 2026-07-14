package com.application.ai.practice.controller;

import com.application.ai.practice.aiservice.AiChatService;
import com.application.ai.practice.model.basic.BaseResult;
import com.application.ai.practice.model.request.ChatRequest;
import dev.langchain4j.service.TokenStream;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;

@RestController
@RequestMapping("/api/aiChat")
@Tag(name = "AI志愿对话接口")
public class AiChatController {

    @Resource
    private AiChatService aiChatService;

    /**
     * SSE流式对话输出
     */
    @GetMapping(value = "/stream/chatAgent")
    @Operation(summary = "AI流式对话（打字机效果）")
    public BaseResult<SseEmitter> chatAgent(@RequestBody ChatRequest request) {
        SseEmitter emitter = new SseEmitter(0L);
        TokenStream tokenStream = aiChatService.streamChat(request.getStudentId(), request.getQuestion());
        tokenStream.onPartialResponse(text -> {
            try {
                emitter.send(SseEmitter.event().data(text));
            } catch (IOException e) {
                emitter.complete();
            }
        }).onCompleteResponse(text -> {
            // 对话结束入库
            try {
                emitter.send(SseEmitter.event().name("end").data("done"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            emitter.complete();
        }).onError(emitter::completeWithError).start();

        return BaseResult.success(emitter);
    }

    @PostMapping("/stream/chatSse")
    public BaseResult<SseEmitter> chatSse(@RequestBody ChatRequest request) {
        SseEmitter emitter = new SseEmitter(60000L);
        try {
            aiChatService.streamChat(request.getStudentId(), request.getQuestion(), emitter);
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
        return BaseResult.success(emitter);
    }

    /**
     * 一次性返回对话结果
     */
    @GetMapping("/normalChat")
    @Operation(summary = "普通一次性对话")
    public BaseResult<String> normalChat(@RequestBody ChatRequest request) {
        String message = aiChatService.normalChat(request.getStudentId(), request.getQuestion());
        return BaseResult.success(message);
    }

    /**
     * 加载知识库的对话
     * @param request
     * @return
     */
    @PostMapping("/syncChat")
    public BaseResult<String> syncChat(@RequestBody ChatRequest request) {
        String answer = aiChatService.syncChat(request.getStudentId(), request.getQuestion());
        return BaseResult.success(answer);
    }

}