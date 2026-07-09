package com.application.ai.practice.controller;

import com.application.ai.practice.model.dto.QueryRequest;
import com.application.ai.practice.util.FilterUtil;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmbeddingController {

    /**
     * 在yaml中进行了配置
     */
    @Autowired
    private EmbeddingModel embeddingModel;

    @Resource
    EmbeddingStore<TextSegment> embeddingStore;

    /**
     * 向向量数据库新增文本记录
     * @return
     */
    @PostMapping("/embedding/add")
    public String add(@RequestParam(name = "message",required = false) String message) {
        String prompt = """
                       咏鸡
                鸡鸣破晓光，红冠映朝阳。
                金羽披霞彩，昂首步高岗。
                """;
        if (StringUtils.isBlank(message)){
            message = prompt;
        }
        TextSegment segment = TextSegment.from(message);
        segment.metadata().put("author", "gogo");
        Embedding content = embeddingModel.embed(segment).content();
        //一定要带上：segment, 否则就会在查询时候查询不到结果.
        String result = embeddingStore.add(content,segment);
        System.out.println("result = " + result);
        return result;
    }

    /**
     * 向量信息查找
     */
    @GetMapping("/embedding/query")
    public String query(@RequestParam(name = "message",required = false) String message) {
        if (StringUtils.isBlank(message)){
            message = "咏鸡说的是什么内容";
        }
        Embedding content = embeddingModel.embed(message).content();
        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(content)
                .maxResults(1) // 返回一条数据
                .build();
        EmbeddingSearchResult<TextSegment> res = embeddingStore.search(embeddingSearchRequest);
        List<EmbeddingMatch<TextSegment>> matches = res.matches();
        if (matches == null || matches.isEmpty()) {
            return "未找到相关结果";
        }
        TextSegment segment = matches.get(0).embedded();
        if (segment == null) {
            return "找到向量但未关联文本段";
        }
        return segment.text();
    }

    /**
     * 向量信息查找，过滤
     */
    @PostMapping("/embedding/filterQuery")
    public String filterQuery(@RequestBody QueryRequest queryRequest) {
        String message = queryRequest.getQuery();
        if (StringUtils.isBlank(message)){
            message = "咏鸡";
        }
        Embedding content = embeddingModel.embed(message).content();
        List<Filter> filterList = FilterUtil.generate(queryRequest.getFilters());
        EmbeddingSearchRequest embeddingSearchRequest = null;
        if (CollectionUtils.isNotEmpty(filterList)){
            embeddingSearchRequest = EmbeddingSearchRequest.builder()
                    .queryEmbedding(content)
                    .filter(filterList.get(0)) // 过滤数据
                    .maxResults(1) // 返回一条数据
                    .build();
        } else {
            embeddingSearchRequest = EmbeddingSearchRequest.builder()
                    .queryEmbedding(content)
                    .maxResults(1) // 返回一条数据
                    .build();
        }
        EmbeddingSearchResult<TextSegment> res = embeddingStore.search(embeddingSearchRequest);
        List<EmbeddingMatch<TextSegment>> matches = res.matches();
        if (matches == null || matches.isEmpty()) {
            return "未找到相关结果";
        }
        TextSegment segment = matches.get(0).embedded();
        if (segment == null) {
            return "找到向量但未关联文本段";
        }
        return segment.text();
    }

}
