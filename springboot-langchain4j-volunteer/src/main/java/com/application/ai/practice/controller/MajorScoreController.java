package com.application.ai.practice.controller;

import com.application.ai.practice.model.basic.BaseResult;
import com.application.ai.practice.model.entity.MajorScore;
import com.application.ai.practice.service.MajorScoreService;
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
@RequestMapping("/api/majorScore")
@Tag(name = "专业分数信息管理")
public class MajorScoreController {

    @Resource
    private MajorScoreService majorScoreService;

    @PostMapping("/add")
    @Operation(summary = "新增专业分数")
    public BaseResult<Boolean> add(@RequestBody MajorScore majorScore) {
        Boolean result = majorScoreService.saveEntity(majorScore);
        return BaseResult.success(result);
    }

    @PutMapping("/update")
    @Operation(summary = "修改专业分数")
    public BaseResult<Boolean> update(@RequestBody MajorScore majorScore) {
        Boolean result = majorScoreService.updateEntity(majorScore);
        return BaseResult.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除专业分数")
    public BaseResult<Boolean> delete(@PathVariable Long id) {
        Boolean result = majorScoreService.removeById(id);
        return BaseResult.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询")
    public BaseResult<MajorScore> get(@PathVariable Long id) {
        MajorScore result = majorScoreService.getById(id);
        return BaseResult.success(result);
    }

    @GetMapping("/list")
    @Operation(summary = "全量列表")
    public BaseResult<List<MajorScore>> list() {
        List<MajorScore> result = majorScoreService.list();
        return BaseResult.success(result);
    }
}