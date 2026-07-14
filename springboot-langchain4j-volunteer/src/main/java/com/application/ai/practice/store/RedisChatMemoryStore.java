package com.application.ai.practice.store;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 自定义会话记忆存储仓库
 */
@Component
public class RedisChatMemoryStore implements ChatMemoryStore {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CHAT_MEMORY_KEY_PREFIX = "chat_memory:";

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String msg = (String) redisTemplate.opsForValue().get(CHAT_MEMORY_KEY_PREFIX + memoryId);
        if (msg == null || "".equals(msg)) {
            return List.of();
        }
        return ChatMessageDeserializer.messagesFromJson(msg);
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        //更新会话消息
        //把list转换成JSON数据
        String s = ChatMessageSerializer.messagesToJson(list);
        redisTemplate.opsForValue().set(CHAT_MEMORY_KEY_PREFIX + memoryId, s, 1, TimeUnit.DAYS);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        redisTemplate.delete(CHAT_MEMORY_KEY_PREFIX + memoryId);
    }
}
