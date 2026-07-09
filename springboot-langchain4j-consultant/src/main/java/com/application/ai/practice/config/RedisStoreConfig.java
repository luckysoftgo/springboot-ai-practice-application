package com.application.ai.practice.config;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.ClassPathDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class RedisStoreConfig {

    /**
     * 在yaml中进行了配置
     */
    @Autowired
    private EmbeddingModel embeddingModel;

    /**
     * Redis 要求有command 'FT._LIST',否则报错：ERR unknown command 'FT._LIST'
     * 不过：yml 配置不影响
     */
    //@Autowired
    //private RedisEmbeddingStore redisEmbeddingStore;

    @Autowired
    private EmbeddingStore <TextSegment> embeddingStore;

    /**
     * 构建向量数据库操作对象
     * @return
     */
    @Bean //只需要执行一次，可以优化
    @ConditionalOnProperty(name = "app.embedding-store.init", havingValue = "true", matchIfMissing = true)
    public EmbeddingStore store() {//embeddingStore的对象, 这个对象的名字不能重复,所以这里使用store
        log.info("初始化向量数据库...");
        //pdf解析器
        ApachePdfBoxDocumentParser documentParser = new ApachePdfBoxDocumentParser();

        //1.使用文档加载器-加载文档进内存
        List<Document> documents = ClassPathDocumentLoader.loadDocuments("content", documentParser);

        // 2. 文档切割：将每个文档按每段进行分割，最大 1000 字符，每次重叠最多 200 个字符
        // 构建文档分割器对象(默认：递归分割器)
        // 参数maxSegmentSizeInChars -- 每个片段最大容纳的字符数
        // 参数maxOverlapSizeInChars -- 两个片段之间重叠字符的个数
        DocumentSplitter splitter = DocumentSplitters.recursive(1000,200);
        //2.构建一个EmbeddingStoreIngestor对象,完成文本数据切割,向量化, 存储
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                //使用redis存储向量数据库
                .embeddingStore(embeddingStore)
                // 为了提高搜索质量，为每个 TextSegment 添加文档名称
                .textSegmentTransformer(textSegment -> TextSegment.from(
                        textSegment.metadata().getString("file_name") + "\n" + textSegment.text(),
                        textSegment.metadata()
                ))
                //使用文档分割器
                .documentSplitter(splitter)
                //使用向量模型
                .embeddingModel(embeddingModel)
                .build();
        ingestor.ingest(documents);
        return embeddingStore;
    }

    /**
     * 构建向量数据库检索对象
     * @return
     */
    @Bean
    public ContentRetriever contentRetriever() {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                //最小余弦相似度的值
                .minScore(0.5)
                //最大的返回结果数量
                .maxResults(3)
                .embeddingModel(embeddingModel)
                .build();
    }

}
