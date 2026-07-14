package com.application.ai.practice.service;

import com.application.ai.practice.mapper.ChatHistoryMapper;
import com.application.ai.practice.model.entity.ChatHistory;
import com.application.ai.practice.service.impl.BasicServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ChatHistoryService extends BasicServiceImpl<ChatHistoryMapper, ChatHistory> {

}
