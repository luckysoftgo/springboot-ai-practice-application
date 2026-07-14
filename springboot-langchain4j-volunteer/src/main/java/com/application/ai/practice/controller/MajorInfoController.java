package com.application.ai.practice.controller;

import com.application.ai.practice.model.basic.BaseResult;
import com.application.ai.practice.model.entity.MajorInfo;
import com.application.ai.practice.service.MajorInfoService;
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
@RequestMapping("/api/majorInfo")
@Tag(name = "专业信息管理")
public class MajorInfoController {

    @Resource
    private MajorInfoService majorInfoService;

    @PostMapping("/add")
    @Operation(summary = "新增专业")
    public BaseResult<Boolean> add(@RequestBody MajorInfo majorInfo) {
        Boolean result = majorInfoService.saveEntity(majorInfo);
        return BaseResult.success(result);
    }

    @PutMapping("/update")
    @Operation(summary = "修改专业")
    public BaseResult<Boolean> update(@RequestBody MajorInfo majorInfo) {
        Boolean result = majorInfoService.updateEntity(majorInfo);
        return BaseResult.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除专业")
    public BaseResult<Boolean> delete(@PathVariable Long id) {
        Boolean result = majorInfoService.removeById(id);
        return BaseResult.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询")
    public BaseResult<MajorInfo> get(@PathVariable Long id) {
        MajorInfo result = majorInfoService.getById(id);
        return BaseResult.success(result);
    }

    @GetMapping("/list")
    @Operation(summary = "全量列表")
    public BaseResult<List<MajorInfo>> list() {
        List<MajorInfo> result = majorInfoService.list();
        return BaseResult.success(result);
    }
}