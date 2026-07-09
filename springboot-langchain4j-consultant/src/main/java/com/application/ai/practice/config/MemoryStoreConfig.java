package com.application.ai.practice.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MemoryStoreConfig {

    /**
     * ----构建向量数据库-操作对象 ----(基础内存版本)----
     * @return
     */
    /*
    @Bean
    public EmbeddingStore eStore() {//embeddingStore的对象, 这个对象的名字不能重复,所以这里使用store
        //1.加载文档进内存（加载的都是MD文档）
        List<Document> documents = ClassPathDocumentLoader.loadDocuments("content-md");

        //2.构建向量数据库操作对象  操作的是内存版本的向量数据库
        InMemoryEmbeddingStore store = new InMemoryEmbeddingStore();

        //3.构建一个EmbeddingStoreIngestor对象,完成文本数据切割,向量化, 存储
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(store)
                //此处使用的是默认的向量模型
                .build();
        ingestor.ingest(documents);
        return store;
    }
     */

    /**
     * ----构建向量数据库-检索对象 ----(基础内存版本)----
     * @param eStore
     * @return
     */
     /*
    @Bean
    public ContentRetriever contentRetriever(EmbeddingStore eStore) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(eStore)
                //最小余弦相似度的值
                .minScore(0.5)
                //最大的返回结果数量
                .maxResults(3)
                //此处使用的是默认的向量模型
                .build();
    }
    */
}
