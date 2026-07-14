package com.application.ai.practice.controller;

import com.application.ai.practice.model.basic.BaseResult;
import com.application.ai.practice.model.entity.CollegeInfo;
import com.application.ai.practice.service.CollegeInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/college")
@Tag(name = "院校信息管理")
public class CollegeInfoController {

    @Resource
    private CollegeInfoService collegeInfoService;

    @PostMapping("/add")
    @Operation(summary = "新增院校")
    public BaseResult<Boolean> add(@RequestBody CollegeInfo collegeInfo) {
        Boolean result =  collegeInfoService.saveEntity(collegeInfo);
        return BaseResult.success(result);
    }

    @PutMapping("/update")
    @Operation(summary = "修改院校")
    public BaseResult<Boolean> update(@RequestBody CollegeInfo collegeInfo) {
        Boolean result =  collegeInfoService.updateEntity(collegeInfo);
        return BaseResult.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除院校")
    public BaseResult<Boolean> delete(@PathVariable Long id) {
        Boolean result =  collegeInfoService.removeById(id);
        return BaseResult.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询")
    public BaseResult<CollegeInfo> get(@PathVariable Long id) {
        CollegeInfo result =  collegeInfoService.getById(id);
        return BaseResult.success(result);
    }

    @GetMapping("/list")
    @Operation(summary = "全量列表")
    public BaseResult<List<CollegeInfo>> list() {
        List<CollegeInfo> result =  collegeInfoService.list();
        return BaseResult.success(result);
    }
}