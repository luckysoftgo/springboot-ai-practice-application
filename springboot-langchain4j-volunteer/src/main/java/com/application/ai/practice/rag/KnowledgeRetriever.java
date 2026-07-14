package com.application.ai.practice.rag;


import com.application.ai.practice.model.entity.KnowledgeDoc;
import com.application.ai.practice.service.KnowledgeDocService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class KnowledgeRetriever {

    private final KnowledgeDocService knowledgeDocService;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    /**
     * 启动时加载所有知识文档并向量化存储
     */
    @PostConstruct
    public void init() {
        List<KnowledgeDoc> docs = knowledgeDocService.list();
        if (CollectionUtils.isEmpty(docs)){
            String[] paths = new String[]{
                    "D:\\7.workspace\\github-code\\springboot-ai-practice-application\\springboot-langchain4j-volunteer\\src\\main\\resources\\content",
                    "D:\\7.workspace\\github-code\\springboot-ai-practice-application\\springboot-langchain4j-volunteer\\src\\main\\resources\\content-md"};
            for (String path : paths){
                knowledgeDocService.importFromPath(path);
            }
        }
        docs = knowledgeDocService.list();
        DocumentSplitter splitter = DocumentSplitters.recursive(500, 50);
        for (KnowledgeDoc doc : docs) {
            Document document = Document.from(doc.getDocContent());
            List<TextSegment> segments = splitter.split(document);
            List<Embedding> embeddings = embeddingModel.embedAll(segments).content();
            embeddingStore.addAll(embeddings, segments);
        }
        log.info("知识库向量化完成，共 {} 个文档", docs.size());
    }

    public String retrieveRelevantContext(String question, int maxResults) {
        Embedding queryEmbedding = embeddingModel.embed(question).content();
        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(maxResults)
                .build();
        EmbeddingSearchResult<TextSegment> result = embeddingStore.search(request);
        return result.matches().stream()
                .map(match -> match.embedded().text())
                .collect(Collectors.joining("\n---\n"));
    }
}