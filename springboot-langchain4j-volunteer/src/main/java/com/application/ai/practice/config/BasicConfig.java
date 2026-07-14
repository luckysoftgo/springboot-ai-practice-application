package com.application.ai.practice.config;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class BasicConfig {

    @Autowired
    private ChatMemoryStore chatMemoryStore;

    /**
     * 构建会话记忆对象-设置20个记忆
     * 存储在内存
     * 所有用户共享
     * @return
     */
    @Bean
    public ChatMemory chatMemory() {
        MessageWindowChatMemory memory = MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();
        return memory;
    }

    /**
     * 构建ChatMemoryProvider对象 （会话记忆+个数） : 会话记忆：每个考生独立会话
     * 存储在redis中
     * 每个用户独立
     * @return
     */
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        ChatMemoryProvider chatMemoryProvider = new ChatMemoryProvider() {
            @Override
            public ChatMemory get(Object memoryId) {
                return MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(20)
                        //使用redis存储会话记忆,见 RedisChatMemoryStore.java
                        .chatMemoryStore(chatMemoryStore)
                        .build();
            }
        };
        return chatMemoryProvider;
    }

    /**
     * RAG检索增强器
     * @param embeddingModel
     * @param store
     * @return
     */
    @Bean
    public RetrievalAugmentor retrievalAugmentor(EmbeddingModel embeddingModel, EmbeddingStore store) {
        EmbeddingStoreContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(store)
                .embeddingModel(embeddingModel)
                .maxResults(5)
                .minScore(0.6)
                .build();
        return DefaultRetrievalAugmentor.builder()
                .contentRetriever(retriever)
                .build();
    }
}
