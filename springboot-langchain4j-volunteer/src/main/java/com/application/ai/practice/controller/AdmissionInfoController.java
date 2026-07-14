package com.application.ai.practice.controller;

import com.application.ai.practice.model.basic.BaseResult;
import com.application.ai.practice.model.basic.PageQuery;
import com.application.ai.practice.model.basic.PageResult;
import com.application.ai.practice.model.entity.AdmissionInfo;
import com.application.ai.practice.service.AdmissionInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
@RequestMapping("/api/admission")
@Tag(name = "录取信息管理")
public class AdmissionInfoController {

    @Resource
    private AdmissionInfoService admissionInfoService;

    @PostMapping("/page")
    public PageResult<AdmissionInfo> page(@RequestBody PageQuery query) {
        Page<AdmissionInfo> page = admissionInfoService.page(new Page<>(query.getPageNum(), query.getPageSize()));
        PageResult<AdmissionInfo> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setRecords(page.getRecords());
        return result;
    }

    @PostMapping("/add")
    @Operation(summary = "新增录取")
    public BaseResult<Boolean> add(@RequestBody AdmissionInfo admissionInfo) {
         Boolean result = admissionInfoService.saveEntity(admissionInfo);
         return BaseResult.success(result);
    }

    @PutMapping("/update")
    @Operation(summary = "修改录取")

    public BaseResult<Boolean> update(@RequestBody AdmissionInfo admissionInfo) {
        Boolean result =  admissionInfoService.updateEntity(admissionInfo);
        return BaseResult.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除录取")
    public BaseResult<Boolean> delete(@PathVariable Long id) {
        Boolean result =  admissionInfoService.removeById(id);
        return BaseResult.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询")
    public BaseResult<AdmissionInfo> get(@PathVariable Long id) {
        AdmissionInfo info =  admissionInfoService.getById(id);
        return BaseResult.success(info);
    }

    @GetMapping("/list")
    @Operation(summary = "全量列表")
    public BaseResult<List<AdmissionInfo>> list() {
        List<AdmissionInfo> list = admissionInfoService.list();
        return BaseResult.success(list);
    }
}