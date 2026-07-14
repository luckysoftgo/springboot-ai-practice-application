package com.application.ai.practice.controller;

import com.application.ai.practice.model.basic.BaseResult;
import com.application.ai.practice.model.entity.ChatHistory;
import com.application.ai.practice.service.ChatHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chatHistory")
@Tag(name = "聊天信息管理")
public class ChatHistoryController {
    @Resource
    private ChatHistoryService chatHistoryService;

    @PostMapping("/add")
    @Operation(summary = "新增聊天")
    public BaseResult<Boolean> add(@RequestBody ChatHistory chatHistory) {
        Boolean result =  chatHistoryService.saveEntity(chatHistory);
        return BaseResult.success(result);
    }

    @PutMapping("/update")
    @Operation(summary = "修改聊天")
    public BaseResult<Boolean> update(@RequestBody ChatHistory chatHistory) {
        Boolean result =  chatHistoryService.updateEntity(chatHistory);
        return BaseResult.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除聊天")
    public BaseResult<Boolean> delete(@PathVariable Long id) {
        Boolean result =  chatHistoryService.removeById(id);
        return BaseResult.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询")
    public BaseResult<ChatHistory> get(@PathVariable Long id) {
        ChatHistory result =  chatHistoryService.getById(id);
        return BaseResult.success(result);
    }

    @GetMapping("/list")
    @Operation(summary = "全量列表")
    public BaseResult<List<ChatHistory>> list() {
        List<ChatHistory> list =  chatHistoryService.list();
        return BaseResult.success(list);
    }
}