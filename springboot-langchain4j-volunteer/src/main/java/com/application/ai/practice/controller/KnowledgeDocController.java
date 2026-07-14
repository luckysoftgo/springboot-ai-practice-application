package com.application.ai.practice.controller;

import com.application.ai.practice.model.basic.BaseResult;
import com.application.ai.practice.model.basic.PageQuery;
import com.application.ai.practice.model.basic.PageResult;
import com.application.ai.practice.model.dto.KnowledgeBatchImport;
import com.application.ai.practice.model.dto.KnowledgeImportResult;
import com.application.ai.practice.model.entity.KnowledgeDoc;
import com.application.ai.practice.service.KnowledgeDocService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.langchain4j.service.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
@Tag(name = "知识信息管理")
public class KnowledgeDocController {

    @Resource
    private KnowledgeDocService knowledgeDocService;

    /**
     * 知识库批量导入Excel
     */
    @PostMapping("/batch/import")
    public BaseResult<KnowledgeImportResult> batchImport(@Valid KnowledgeBatchImport dto) {
        KnowledgeImportResult result = knowledgeDocService.batchImport(dto);
        return BaseResult.success(result);
    }

    @PostMapping("/files")
    public BaseResult<String> importFiles(@RequestParam("directory") String directoryPath) {
        // 扫描指定目录下的所有 .pdf 和 .md 文件
        knowledgeDocService.importFromPath(directoryPath);
        return BaseResult.success("导入完成");
    }

    /**
     * 分页查询知识库列表
     */
    @PostMapping("/page/list")
    public BaseResult<PageResult<KnowledgeDoc>> pageList(@RequestBody @Valid PageQuery pageQuery) {
        IPage<KnowledgeDoc> page = knowledgeDocService.pageList(pageQuery);
        return BaseResult.success(PageResult.build(page));
    }

    @PostMapping("/add")
    @Operation(summary = "新增知识")
    public BaseResult<Boolean> add(@RequestBody KnowledgeDoc knowledgeDoc) {
        Boolean result = knowledgeDocService.saveEntity(knowledgeDoc);
        return BaseResult.success(result);
    }

    @PutMapping("/update")
    @Operation(summary = "修改知识")
    public BaseResult<Boolean> update(@RequestBody KnowledgeDoc knowledgeDoc) {
        Boolean result = knowledgeDocService.updateEntity(knowledgeDoc);
        return BaseResult.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除知识")
    public BaseResult<Boolean> delete(@PathVariable Long id) {
        Boolean result = knowledgeDocService.removeById(id);
        return BaseResult.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询")
    public BaseResult<KnowledgeDoc> get(@PathVariable Long id) {
        KnowledgeDoc result = knowledgeDocService.getById(id);
        return BaseResult.success(result);
    }

    @GetMapping("/list")
    @Operation(summary = "全量列表")
    public BaseResult<List<KnowledgeDoc>> list() {
        List<KnowledgeDoc> result = knowledgeDocService.list();
        return BaseResult.success(result);
    }
}